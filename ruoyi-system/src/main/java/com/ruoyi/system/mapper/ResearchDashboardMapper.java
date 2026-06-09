package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 科研首页看板统计Mapper接口.
 */
public interface ResearchDashboardMapper
{
    int countHorizontalByStates(@Param("states") List<String> states,
                                @Param("year") String year);

    int countHorizontalByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                   @Param("states") List<String> states,
                                                   @Param("year") String year);

    int countHorizontalByDeptAndStates(@Param("deptId") Long deptId,
                                       @Param("states") List<String> states,
                                       @Param("year") String year);

    int countHorizontalByUserAndStates(@Param("userId") Long userId,
                                       @Param("states") List<String> states,
                                       @Param("year") String year);

    int countReamountByStates(@Param("states") List<String> states,
                              @Param("year") String year);

    int countReamountByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                 @Param("states") List<String> states,
                                                 @Param("year") String year);

    int countReamountByDeptAndStates(@Param("deptId") Long deptId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    int countReamountByUserAndStates(@Param("userId") Long userId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    int countVerticalByStates(@Param("states") List<String> states,
                              @Param("year") String year);

    int countVerticalByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                 @Param("states") List<String> states,
                                                 @Param("year") String year);

    int countVerticalByDeptAndStates(@Param("deptId") Long deptId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    int countVerticalByUserAndStates(@Param("userId") Long userId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    int countPaperByStates(@Param("states") List<String> states,
                           @Param("year") String year);

    int countPaperByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                              @Param("states") List<String> states,
                                              @Param("year") String year);

    int countPaperByDeptAndStates(@Param("deptId") Long deptId,
                                  @Param("states") List<String> states,
                                  @Param("year") String year);

    int countPaperByUserAndStates(@Param("userId") Long userId,
                                  @Param("states") List<String> states,
                                  @Param("year") String year);

    int countAchievementByStates(@Param("states") List<String> states,
                                 @Param("year") String year);

    int countAchievementByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                    @Param("states") List<String> states,
                                                    @Param("year") String year);

    int countAchievementByDeptAndStates(@Param("deptId") Long deptId,
                                        @Param("states") List<String> states,
                                        @Param("year") String year);

    int countAchievementByUserAndStates(@Param("userId") Long userId,
                                        @Param("states") List<String> states,
                                        @Param("year") String year);

    int countTextbookByStates(@Param("states") List<String> states,
                              @Param("year") String year);

    int countTextbookByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                 @Param("states") List<String> states,
                                                 @Param("year") String year);

    int countTextbookByDeptAndStates(@Param("deptId") Long deptId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    int countTextbookByUserAndStates(@Param("userId") Long userId,
                                     @Param("states") List<String> states,
                                     @Param("year") String year);

    int countPatentByStates(@Param("states") List<String> states,
                            @Param("year") String year);

    int countPatentByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                               @Param("states") List<String> states,
                                               @Param("year") String year);

    int countPatentByDeptAndStates(@Param("deptId") Long deptId,
                                   @Param("states") List<String> states,
                                   @Param("year") String year);

    int countPatentByUserAndStates(@Param("userId") Long userId,
                                   @Param("states") List<String> states,
                                   @Param("year") String year);

    int countRewardByStates(@Param("states") List<String> states,
                            @Param("year") String year);

    int countRewardByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                               @Param("states") List<String> states,
                                               @Param("year") String year);

    int countRewardByDeptAndStates(@Param("deptId") Long deptId,
                                   @Param("states") List<String> states,
                                   @Param("year") String year);

    int countRewardByUserAndStates(@Param("userId") Long userId,
                                   @Param("states") List<String> states,
                                   @Param("year") String year);

    int countLectureByStates(@Param("states") List<String> states,
                             @Param("year") String year);

    int countLectureByDeptAndStatesWithChildren(@Param("deptId") Long deptId,
                                                @Param("states") List<String> states,
                                                @Param("year") String year);

    int countLectureByDeptAndStates(@Param("deptId") Long deptId,
                                    @Param("states") List<String> states,
                                    @Param("year") String year);

    int countLectureByUserAndStates(@Param("userId") Long userId,
                                    @Param("states") List<String> states,
                                    @Param("year") String year);
}
