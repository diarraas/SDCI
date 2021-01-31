# Copyright (c) 2015 SONATA-NFV and Paderborn University
# ALL RIGHTS RESERVED.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#	http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Neither the name of the SONATA-NFV, Paderborn University
# nor the names of its contributors may be used to endorse or promote
# products derived from this software without specific prior written
# permission.
#
# This work has been performed in the framework of the SONATA project,
# funded by the European Commission under Grant number 671517 through
# the Horizon 2020 and 5G-PPP programmes. The authors would like to
# acknowledge the contributions of their colleagues of the SONATA
# partner consortium (www.sonata-nfv.eu).
import logging
from mininet.log import setLogLevel
from emuvim.dcemulator.net import DCNetwork
from emuvim.api.rest.rest_api_endpoint import RestApiEndpoint
from emuvim.api.openstack.openstack_api_endpoint import OpenstackApiEndpoint
import time

TOPOS = {'mytopo':(lambda:create_topology())}

logging.basicConfig(level=logging.INFO)
setLogLevel('info')  # set Mininet loglevel
logging.getLogger('werkzeug').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.base').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.compute').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.keystone').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.nova').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.neutron').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.heat').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.heat.parser').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.glance').setLevel(logging.DEBUG)
logging.getLogger('api.openstack.helper').setLevel(logging.DEBUG)


def create_topology():
	net = DCNetwork(monitor=False, enable_learning=True,autoSetMacs=True)

	dc1 = net.addDatacenter("dc1")
	# add OpenStack-like APIs to the emulated DC
	api1 = OpenstackApiEndpoint("0.0.0.0", 6001)
	api1.connect_datacenter(dc1)
	api1.start()
	api1.connect_dc_network(net)
	# add the command line interface endpoint to the emulated DC (REST API)
	rapi1 = RestApiEndpoint("0.0.0.0", 5001)
	rapi1.connectDCNetwork(net)
	rapi1.connectDatacenter(dc1)
	rapi1.start()

	s1 = net.addSwitch('s1')
	s2 = net.addSwitch('s2')
	s3 = net.addSwitch('s3')
	s4 = net.addSwitch('s4')

	srv = net.addDocker('srv', ip='192.168.0.1', dimage="server:latest")
	time.sleep(5)
	gwi = net.addDocker('gwi', ip='192.168.0.254', dimage="gateway:latest")
	time.sleep(5)
	#First cluster
	gwf1 = net.addDocker('gwf1', ip='192.168.0.252', dimage="gwf1:latest")
	device11 = net.addDocker('device11', ip='192.168.0.241', dimage="device1:latest")
	time.sleep(5)
	device12 = net.addDocker('device12', ip='192.168.0.242', dimage="device1:latest")
	device13 = net.addDocker('device13', ip='192.168.0.243', dimage="device1:latest")
	
	#Second cluster
	gwf2 = net.addDocker('gwf2', ip='192.168.0.251', dimage="gwf2:latest")
	time.sleep(5)
	device21 = net.addDocker('device21', ip='192.168.0.231', dimage="device2:latest")
	device22 = net.addDocker('device22', ip='192.168.0.232', dimage="device2:latest")
	device23 = net.addDocker('device23', ip='192.168.0.233', dimage="device2:latest")
	
	#Third cluster
	gwf3 = net.addDocker('gwf3', ip='192.168.0.250', dimage="gwf3:latest")
	time.sleep(5)
	device31 = net.addDocker('device31', ip='192.168.0.221', dimage="device3:latest")
	device32 = net.addDocker('device32', ip='192.168.0.222', dimage="device3:latest")
	device33 = net.addDocker('device33', ip='192.168.0.223', dimage="device3:latest")
		
	#Switch links
	net.addLink(s1, s2)
	net.addLink(s2, s3)
	net.addLink(s2, s4)
	net.addLink(s4,dc1)
	net.addLink(s1, srv)
	net.addLink(s2, gwi)

	#First cluster link
	net.addLink(s3, gwf1)
	net.addLink(gwf1,device11)
	net.addLink(gwf1,device12)
	net.addLink(gwf1,device13)
	
	#Second cluster link
	net.addLink(s3, gwf2)
	net.addLink(gwf2,device21)
	net.addLink(gwf2,device22)
	net.addLink(gwf2,device23)
	
	#Third cluster link
	net.addLink(s4, gwf3)
	net.addLink(gwf3,device31)
	net.addLink(gwf3,device32)
	net.addLink(gwf3,device33)
	

	net.start()
	"""
  srv.cmd("node srv/server.js --local_ip '192.168.0.1' --local_port 8080 --local_name 'srv'")
  time.sleep(2)

  gwi.cmd("node srv/gateway.js --local_ip '192.168.0.254' --local_port 8181 --local_name 'gwi1' --remote_ip '192.168.0.1' --remote_port 8080 --remote_name 'srv'")
  time.sleep(2)

  gwf1.cmd("node srv/gateway.js --local_ip '192.168.0.252' --local_port 8282 --local_name 'gwf1' --remote_ip '192.168.0.254' --remote_port 8181 --remote_name 'gwi1'")
  time.sleep(2)
  device11.cmd("node srv/device.js --local_ip '192.168.0.241' --local_port 9001 --local_name 'device11' --remote_ip '192.168.0.252' --remote_port 8282 --remote_name 'gwf1' --send_period 3000")
  device12.cmd("node srv/device.js --local_ip '192.168.0.242' --local_port 9001 --local_name 'device12' --remote_ip '192.168.0.252' --remote_port 8282 --remote_name 'gwf1' --send_period 3000")
  device13.cmd("node srv/device.js --local_ip '192.168.0.243' --local_port 9001 --local_name 'device13' --remote_ip '192.168.0.252' --remote_port 8282 --remote_name 'gwf1' --send_period 3000")

  gwf2.cmd("node srv/gateway.js --local_ip '192.168.0.251' --local_port 8282 --local_name 'gwf2' --remote_ip '192.168.0.254' --remote_port 8181 --remote_name 'gwi1'")
  time.sleep(2)
  device21.cmd("node srv/device.js --local_ip '192.168.0.231' --local_port 9001 --local_name 'device21' --remote_ip '192.168.0.251' --remote_port 8282 --remote_name 'gwf2' --send_period 3000")
  device22.cmd("node srv/device.js --local_ip '192.168.0.232' --local_port 9001 --local_name 'device22' --remote_ip '192.168.0.251' --remote_port 8282 --remote_name 'gwf2' --send_period 3000")
  device23.cmd("node srv/device.js --local_ip '192.168.0.233' --local_port 9001 --local_name 'device23' --remote_ip '192.168.0.251' --remote_port 8282 --remote_name 'gwf2' --send_period 3000")

  gwf3.cmd("node srv/gateway.js --local_ip '192.168.0.250' --local_port 8282 --local_name 'gwf3' --remote_ip '192.168.0.254' --remote_port 8181 --remote_name 'gwi1'")
  time.sleep(2)
  device31.cmd("node srv/device.js --local_ip '192.168.0.221' --local_port 9001 --local_name 'device31' --remote_ip '192.168.0.250' --remote_port 8282 --remote_name 'gwf3' --send_period 3000")
  device32.cmd("node srv/device.js --local_ip '192.168.0.222' --local_port 9001 --local_name 'device32' --remote_ip '192.168.0.250' --remote_port 8282 --remote_name 'gwf3' --send_period 3000")
  device33.cmd("node srv/device.js --local_ip '192.168.0.221' --local_port 9001 --local_name 'device33' --remote_ip '192.168.0.250' --remote_port 8282 --remote_name 'gwf3' --send_period 3000")
		"""
	net.CLI()
	# when the user types exit in the CLI, we stop the emulator
	net.stop()


def main():
	create_topology()


if __name__ == '__main__':
	main()
