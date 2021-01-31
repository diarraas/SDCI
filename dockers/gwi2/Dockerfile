FROM node:12-slim

WORKDIR /

RUN     apt-get update
RUN     apt-get install -y \
        net-tools \
        iputils-ping \
        iproute2 \
	curl \
	tcpdump
RUN	mv /usr/sbin/tcpdump /usr/bin/tcpdump  
RUN     npm install express
RUN	npm install request
RUN     npm install systeminformation
RUN     npm install yargs
ADD	http://homepages.laas.fr/smedjiah/tmp/gateway.js /srv

ENV VIM_EMU_CMD "echo 'Welcome to GWI!'"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container GWI now.'"

ENTRYPOINT    node /srv/gateway.js --local_ip "192.168.0.253" --local_port 8181 --local_name "gwi2" --remote_ip "192.168.0.1" --remote_port 8080 --remote_name "srv" ; tail -f /dev/null


