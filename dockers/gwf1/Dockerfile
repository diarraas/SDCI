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

ENV VIM_EMU_CMD "echo 'Welcome to GWF1!'"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container GWF1 now.'"

ENTRYPOINT    node /srv/gateway.js --local_ip "192.168.0.250" --local_port 8282 --local_name "gwf1" --remote_ip "192.168.0.254" --remote_port 8181 --remote_name "gwi" ; tail -f /dev/null


