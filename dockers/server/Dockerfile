FROM node:12-slim

WORKDIR /

RUN     apt-get update
RUN     apt-get install -y \
        net-tools \
        iputils-ping \
        iproute2 \
	curl  \
	tcpdump
RUN	mv /usr/sbin/tcpdump /usr/bin/tcpdump
RUN     npm install express
RUN     npm install systeminformation
RUN     npm install yargs
ADD	http://homepages.laas.fr/smedjiah/tmp/server.js /srv

ENV VIM_EMU_CMD "echo 'Welcome to Server!'"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container Server now.'"

ENTRYPOINT     node srv/server.js --local_ip "192.168.0.1" --local_port 8080 --local_name "srv"
