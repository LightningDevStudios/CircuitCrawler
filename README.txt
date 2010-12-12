HOW TO COMMIT CHANGES:
-right click on the repository folder, click on "SVN Commit"
-MAKE SURE YOU SAY WHAT YOU CHANGED IN THE MESSAGE TEXTBOX
-check which files you want to commit (use select/deselect all checkbox if you want it all commited)
-press OK.

OTHER STUFF YOU SHOULD KNOW:

1) DO NOT EDIT ANYTHING DIRECTLY FROM THE /trunk FOLDER! IT IS MEANT TO BE A FULLY WORKING COPY ALL THE TIME.
	- instead, make a branch. Right click the repository folder (where you first checked out) go to the TortoiseSVN menu, click "Branch/tag..."
	- You will create your own branch under /branches. Feel free to edit the shit out of your own branch. If you fuck up, you can always
	- remove your branch and create a new one from the current trunk.

2) IF YOU HAVE A FULLY WORKING UPDATE YOU WISH TO INCORPORATE
	- right click the repository folder, go to the TortoiseSVN menu, click "Merge". From here, the interface should be simple enough to merge
	- your branch back with the trunk.

3) IF YOU FEEL WE HAVE COMPLETED SOMETHING BIG AND WISH TO MAKE A BACKUP
	- again, TortoiseSVN menu, "Branch/tag..." but this time, make a tag. Don't make too many of these, but do make them. This way we can 
	- always fall back on an older version if we make some huge mistake.