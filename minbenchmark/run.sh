docker build -t stor.highloadcup.ru/it_one_j22_qual/penguin_climber .
# docker run --mount "type=bind,src=$(pwd)/sample_4.zip,dst=/opt/mount-point" --mount "type=bind,src=$(pwd)/../result,dst=/opt/client/tmp" --rm -it -p 9081:9081 stor.highloadcup.ru/hlc/it1_qual_checker:client
# docker run --mount "type=bind,src=$(pwd)/../result/minbenchmark-0.0.1-SNAPSHOT.jar,dst=/opt/mount-point" --rm -it -p 9081:9081 stor.highloadcup.ru/hlc/it1_qual_checker:client
# docker run --mount "type=bind,src=$(pwd)/../result/compiled.jar,dst=/opt/mount-point" --rm -it -p 9081:9081 stor.highloadcup.ru/hlc/it1_qual_checker:client
docker push stor.highloadcup.ru/it_one_j22_qual/penguin_climber
