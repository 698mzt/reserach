package com.ruoyi.system.service;

import com.ruoyi.system.domain.SciProjectScoreCfg;
import com.ruoyi.system.mapper.SciTec_traScoreCfgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SciTecTraScoreCalculator
{
    private final SciTec_traScoreCfgMapper sciTecTraScoreCfgMapper;

    @Autowired
    public SciTecTraScoreCalculator(SciTec_traScoreCfgMapper sciTecTraScoreCfgMapper)
    {
        this.sciTecTraScoreCfgMapper = sciTecTraScoreCfgMapper;
    }

    public Map<Integer, ScoreDetail> calculateScoreDetails(String amountText)
    {
        BigDecimal amount = parseDecimal(amountText);
        if (amount == null)
        {
            return Collections.emptyMap();
        }
        return calculateScoreDetails(amount);
    }

    public Map<Integer, ScoreDetail> calculateScoreDetails(BigDecimal amount)
    {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            return Collections.emptyMap();
        }

        List<ScoreBand> scoreBands = loadScoreBands();
        if (scoreBands.isEmpty())
        {
            return Collections.emptyMap();
        }

        ResolvedBand resolvedBand = resolveBand(amount, scoreBands);
        if (resolvedBand == null)
        {
            return Collections.emptyMap();
        }

        Map<Integer, ScoreDetail> result = new LinkedHashMap<>();
        for (SciProjectScoreCfg cfg : resolvedBand.band.userConfigs)
        {
            Integer userOrder = parseInteger(cfg.getUserOrder());
            if (userOrder == null)
            {
                continue;
            }

            int totalScore = scaleScore(cfg.getTotalScore(), amount, resolvedBand.baseAmount);
            int startScore = scaleScore(cfg.getStartScore(), amount, resolvedBand.baseAmount);
            int endScore = scaleScore(cfg.getEndScore(), amount, resolvedBand.baseAmount);
            result.put(userOrder, new ScoreDetail(userOrder, totalScore, startScore, endScore));
        }
        return result;
    }

    private List<ScoreBand> loadScoreBands()
    {
        List<SciProjectScoreCfg> configs = sciTecTraScoreCfgMapper.selectScoreConfigsForCalculation();
        if (configs == null || configs.isEmpty())
        {
            return Collections.emptyList();
        }

        Map<String, ScoreBand> groupedBands = new LinkedHashMap<>();
        for (SciProjectScoreCfg cfg : configs)
        {
            BigDecimal fundsMin = parseDecimal(cfg.getFundsMin());
            BigDecimal fundsMax = parseDecimal(cfg.getFundsMax());
            if (fundsMin == null || fundsMax == null)
            {
                continue;
            }

            String key = fundsMin.toPlainString() + "|" + fundsMax.toPlainString();
            ScoreBand scoreBand = groupedBands.get(key);
            if (scoreBand == null)
            {
                scoreBand = new ScoreBand(fundsMin, fundsMax);
                groupedBands.put(key, scoreBand);
            }
            scoreBand.userConfigs.add(cfg);
        }
        return new ArrayList<>(groupedBands.values());
    }

    private ResolvedBand resolveBand(BigDecimal amount, List<ScoreBand> scoreBands)
    {
        ScoreBand firstBand = scoreBands.get(0);
        if (amount.compareTo(firstBand.fundsMin) < 0)
        {
            return new ResolvedBand(firstBand, firstBand.fundsMin);
        }

        for (ScoreBand scoreBand : scoreBands)
        {
            if (scoreBand.isOpenEnded())
            {
                if (amount.compareTo(scoreBand.fundsMin) >= 0)
                {
                    return new ResolvedBand(scoreBand, scoreBand.fundsMin);
                }
                continue;
            }

            if (amount.compareTo(scoreBand.fundsMin) >= 0 && amount.compareTo(scoreBand.fundsMax) < 0)
            {
                return new ResolvedBand(scoreBand, scoreBand.fundsMin);
            }
        }

        ScoreBand lastBand = scoreBands.get(scoreBands.size() - 1);
        if (lastBand.isOpenEnded())
        {
            return new ResolvedBand(lastBand, lastBand.fundsMin);
        }
        return new ResolvedBand(lastBand, lastBand.fundsMin);
    }

    private int scaleScore(String baseScoreText, BigDecimal amount, BigDecimal baseAmount)
    {
        BigDecimal baseScore = parseDecimal(baseScoreText);
        if (baseScore == null || baseAmount == null || baseAmount.compareTo(BigDecimal.ZERO) <= 0)
        {
            return 0;
        }

        return amount.multiply(baseScore)
                .divide(baseAmount, 0, RoundingMode.HALF_UP)
                .intValue();
    }

    private BigDecimal parseDecimal(String value)
    {
        if (value == null)
        {
            return null;
        }
        String normalized = value.trim();
        if (normalized.isEmpty())
        {
            return null;
        }
        return new BigDecimal(normalized);
    }

    private Integer parseInteger(String value)
    {
        if (value == null)
        {
            return null;
        }
        String normalized = value.trim();
        if (normalized.isEmpty())
        {
            return null;
        }
        return Integer.parseInt(normalized);
    }

    private static final class ScoreBand
    {
        private final BigDecimal fundsMin;
        private final BigDecimal fundsMax;
        private final List<SciProjectScoreCfg> userConfigs = new ArrayList<>();

        private ScoreBand(BigDecimal fundsMin, BigDecimal fundsMax)
        {
            this.fundsMin = fundsMin;
            this.fundsMax = fundsMax;
        }

        private boolean isOpenEnded()
        {
            return fundsMax.compareTo(BigDecimal.ZERO) <= 0;
        }
    }

    private static final class ResolvedBand
    {
        private final ScoreBand band;
        private final BigDecimal baseAmount;

        private ResolvedBand(ScoreBand band, BigDecimal baseAmount)
        {
            this.band = band;
            this.baseAmount = baseAmount;
        }
    }

    public static final class ScoreDetail
    {
        private final int userOrder;
        private final int totalScore;
        private final int startScore;
        private final int endScore;

        public ScoreDetail(int userOrder, int totalScore, int startScore, int endScore)
        {
            this.userOrder = userOrder;
            this.totalScore = totalScore;
            this.startScore = startScore;
            this.endScore = endScore;
        }

        public int getUserOrder()
        {
            return userOrder;
        }

        public int getTotalScore()
        {
            return totalScore;
        }

        public int getStartScore()
        {
            return startScore;
        }

        public int getEndScore()
        {
            return endScore;
        }
    }
}
