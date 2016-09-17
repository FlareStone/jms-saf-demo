connect('weblogic','welcome1', 't3://192.168.31.174:7001')

edit()
startEdit()

cd('/')
cmo.createJMSServer('DemoJMSServer')

cd('/JMSServers/DemoJMSServer')
set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))

activate()

startEdit()

cd('/')
cmo.createJMSSystemResource('DemoSystemModule')

cd('/JMSSystemResources/DemoSystemModule')
set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))

activate()

startEdit()

cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule')
cmo.createConnectionFactory('DemoConnectionFactory')

cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory')
cmo.setJNDIName('democf')

cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory/SecurityParams/DemoConnectionFactory')
cmo.setAttachJMSXUserId(false)
cmo.setSecurityPolicy('ThreadBased')

cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory/ClientParams/DemoConnectionFactory')
cmo.setClientIdPolicy('Restricted')
cmo.setSubscriptionSharingPolicy('Exclusive')
cmo.setMessagesMaximum(10)

cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory/TransactionParams/DemoConnectionFactory')
cmo.setXAConnectionFactoryEnabled(true)

cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/ConnectionFactories/DemoConnectionFactory')
cmo.setDefaultTargetingEnabled(true)

activate()

startEdit()

cd('/JMSSystemResources/DemoSystemModule')
cmo.createSubDeployment('DemoQueue')

startEdit()

cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule')
cmo.createQueue('DemoQueue')

cd('/JMSSystemResources/DemoSystemModule/JMSResource/DemoSystemModule/Queues/DemoQueue')
cmo.setJNDIName('queue1')
cmo.setSubDeploymentName('DemoQueue')

cd('/JMSSystemResources/DemoSystemModule/SubDeployments/DemoQueue')
set('Targets',jarray.array([ObjectName('com.bea:Name=DemoJMSServer,Type=JMSServer')], ObjectName))

activate()

disconnect()
exit()