#!/bin/bash
echo "--------\$#="$#"--------"
if [[ $#>0 ]]; then
	#statements
	flash="$1"
	echo "--------$flash--------"
	file_prefix=${flash%.*}
	echo "--------$file_prefix--------"
	if [[ "${flash##*.}" = "tar" ]]; then
		#statements
		echo "--------tar file--------"
		tar xvf "$flash"
		flash_shell="$file_prefix/flash_all_except_storage.sh"
		echo "--------flash_shell=$flash_shell--------"
		if [[ -f "$flash_shell" ]]; then
			#statements
			if [[ "${flash_shell##*.}" = "sh" ]]; then
				#statements
				echo "--------chmod file--------"
				chmod a+x "$flash_shell"
				echo "--------reboot device--------"
				adb reboot bootloader
				sleep 1
				echo "--------flash device--------"
				./"$flash_shell"
				echo "--------delete tar file--------"
				rm "$flash"	
				echo "--------delete folder--------"
				rm -r "$file_prefix/"
				echo "--------flash done--------"
			else
				echo "--------not a  shell file--------"
			fi			
		else
			echo "--------file not exist--------"
		fi
	else
		echo "--------not a tar file--------"
	fi
else
	echo "--------need a argv--------"
fi