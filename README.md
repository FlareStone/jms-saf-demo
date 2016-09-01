## Prepare Environment

1. create wls domain
2. modify bin/common_env.sh according to your environment
3. modify config/default.properties according to your requirements
4. start wls domain
5. update domain with command: ./main.sh u
6. generate saf client configuration file with command: ./main.sh c
7. generate  encrypted password with command: ./main.sh p
8. update saf client configuration file with password generated by above step.

## Usage

Show help:

    java -jar jms-saf.jar

Send messages example:

    java -jar jms-saf.jar -r s -c saf.properties -n 100000 -m hello! -i 1
    

PS: if -i is 0, then it will send batch message with no wait.

Receive message example:
    
    java -jar jms-saf.jar -r r


Compact store:

    ./main.sh z
    
PS: jms-saf.jar is merged from  wlfullclient.jar & commmons-cli-1.3.1.jar & this project which generated by Intellij Idea.

Tested on WLS12.2.1.1.0.

## Known Issues

1. paging setting doesn't work