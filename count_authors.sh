#!/bin/bash

# 定义统计函数（复用逻辑，减少重复代码）
count_lines() {
  local author=$1
  local filter=$2
  # 执行统计并返回结果（新增、删除、净增）
  eval "git log --author='$author' --pretty=tformat: --numstat $filter | awk '{
    add += \$1;
    del += \$2;
    net += \$1 - \$2;
  } END {
    printf \"%d %d %d\", add, del, net;
  }'"
}

# 主循环：支持多次选择
while true; do
  # 显示菜单
  echo -e "\n===== 代码行数统计工具 ====="
  echo "请选择要统计的文件类型（输入数字）："
  echo "1. 全部文件"
  echo "2. 仅 Java 文件（.java）"
  echo "3. 仅 HTML 文件（.html）"
  echo "4. 仅 XML 文件（.xml）"
  echo "5. 退出程序"
  read -p "请输入选项（1-5）：" choice

  # 验证输入有效性
  if [[ ! $choice =~ ^[1-5]$ ]]; then
    echo "输入无效！请输入1-5之间的数字。"
    read -p "按任意键继续..."
    continue  # 回到菜单重新选择
  fi

  # 处理退出选项
  if [ $choice -eq 5 ]; then
    echo "程序已退出，感谢使用！"
    exit 0
  fi

  # 根据选择设置过滤条件和类型名称
  case $choice in
    1) 
      filter=""
      type_name="全部文件"
      ;;
    2) 
      filter="| grep '\.java$'"
      type_name="Java文件（.java）"
      ;;
    3) 
      filter="| grep '\.html$'"
      type_name="HTML文件（.html）"
      ;;
    4) 
      filter="| grep '\.xml$'"
      type_name="XML文件（.xml）"
      ;;
  esac

  # 获取作者列表并统计
  authors=$(git log --pretty="%an" | sort -u)
  echo -e "\n===== 开始统计：$type_name ====="
  
  for author in $authors; do
    echo -e "\n----- 作者：$author -----"
    # 调用统计函数获取结果
    result=$(count_lines "$author" "$filter")
    # 解析结果（新增、删除、净增）
    add=$(echo $result | awk '{print $1}')
    del=$(echo $result | awk '{print $2}')
    net=$(echo $result | awk '{print $3}')
    # 输出统计信息
    printf "新增行数：%d\n删除行数：%d\n净增行数：%d\n" $add $del $net
  done

  # 统计完成，等待用户确认后返回菜单
  read -p $'\n当前类型统计完成，按任意键返回菜单...'
done