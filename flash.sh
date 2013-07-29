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
				echo "Do u want to delete the tar file(Enter 1 for sure.)?"
				read delete_tar
				if [[ "$delete_tar" = 1 ]]; then
					#statements
					echo "delete_tar"
					rm -r "$flash"	
				else
					echo "do not delete_tar"
				fi
				echo "Do u want to delete folder(Enter 1 for sure.)?"
				read delete_folder
				if [[ "$delete_folder" = 1 ]]; then
					#statements
					echo "delete_folder"
					rm -r "$file_prefix/"
				else
					echo "do not delete_folder"
				fi
				echo "flash done"				
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