pushd src\main\webapp
cmd /c ng build --configuration=production
xcopy /y /s dist\open-event\* ..\resources\static
popd

cmd /c gradle clean jibDockerBuild

cmd /c docker push iee1394/open-event:latest
