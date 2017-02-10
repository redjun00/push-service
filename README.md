# push-service

####개요
device token값을 받아서 db에 저장하고 일정 시간 마다 해당 device에 push 메시지가 갈 수 있도록 요청을 보내는 서버.

####도구
spring boot - 1.5.1.RELEASE<br/>
gradle - 2.4<br/>
mysql - Ver 14.14 Distrib 5.7.16, for Linux (x86_64) using  EditLine wrapper<br/>
flyway - 4.0.3<br/>
jpa - 1.5.1.RELEASE

####사전 작업
mysql로컬에 root 계정으로 example db를 만들어 놓는다.
intellij에 db를 연결한다.<br/>
flyway migration을 실행해서 테이블을 만들어 놓는다.

####요청
curl -X POST http://localhost:8080/push/123