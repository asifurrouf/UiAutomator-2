#!/bin/bash
echo "argvs = $#"
if [[ $# = 1 ]]; then
	#statements
	echo "1pacakge name = $1"
	read monkey_times
	echo "monkey_times = $monkey_times"
	adb shell monkey -p "$1" -s 100 "$monkey_times"
elif [[ $# = 2 ]]; then
	#statements
	echo "2pacakge name = $1"
	monkey_times="$2"
	echo "monkey_times = $monkey_times"
	adb shell monkey -p "$1" -s 100 "$monkey_times"
elif [[ $# -le 0 ]]; then
	#statements
	echo "pls give the pacakge name"
else
	echo "too much argvs given"	
fi

# -ne —比较两个参数是否不相等
# -lt —参数1是否小于参数2
# -le —参数1是否小于等于参数2
# -gt —参数1是否大于参数2
# -ge —参数1是否大于等于参数2
# -f — 检查某文件是否存在（例如，if [ -f "filename" ]）
# -d — 检查目录是否存在