FROM node:12-slim

WORKDIR /

RUN     apt-get update
RUN     apt-get install -y \
        net-tools \
        iputils-ping \
        iproute2 \  
	curl
RUN     npm install express
RUN	npm install request
RUN     npm install systeminformation
RUN     npm install yargs
ADD	http://homepages.laas.fr/smedjiah/tmp/gateway.js /srv
ADD	http://homepages.laas.fr/smedjiah/tmp/device.js /srv

ENV VIM_EMU_CMD "echo 'Welcome to Device!'"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container Device now.'"

ENTRYPOINT node /srv/device.js --local_ip "127.0.0.1" --local_port 9001 --local_name "dev1" --remote_ip "192.168.0.251" --remote_port 8282 --remote_name "gwf2" --send_period 3000 ; tail -f /dev/null



