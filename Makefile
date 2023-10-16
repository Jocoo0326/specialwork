apkDir=~/Temp

releaseApk: clear release getLatestApk

uatApk: clear uat getLatestApk

release:
	./gradlew copyAndRenameReleaseApk

uat:
	./gradlew copyAndRenameUatApk

getLatestApk:
	@echo "new Apk: $(shell ls -t ${apkDir}/*.apk | head -n1 | cut -d/ -f5)"

clear:
	rm -f ~/Temp/specialwork*.apk