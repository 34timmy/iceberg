# REST API for coffee machine

To launch the project you need: 
1. Maven 
2. Java 8

How to run:
1. Download the project
2. Run **mvn clean install** from root project path
3. In "target" folder open console
4. Run **java -jar coffeemachine-1.jar** in console

For testing you can use Postman:
 - import "coffeemachine.postman_collection.json" file in Postman and use available requests
 
 Requests:
 - Create coffee (coffeType = americano/espresso/capuchino)
 curl -X GET \
   'http://localhost:8080/do?coffeeType=americano' \
   -H 'Cache-Control: no-cache' \
   -H 'Postman-Token: 68ff7ac9-8e64-4716-b626-0d8e24e7ac48'
  
  - Update resources (beans = int, milk = int, water = int)
  curl -X GET \
    'http://localhost:8080/update?beans=0&milk=0&water=500' \
    -H 'Cache-Control: no-cache' \
    -H 'Postman-Token: dc5bff95-adfa-413c-8e14-eefc76040e7b' 
  
  - Clean coffee machine
  curl -X GET \
    http://localhost:8080/clean \
    -H 'Cache-Control: no-cache' \
    -H 'Postman-Token: cfa75ff9-7579-4e6a-8e96-338d88f479c5'
    
  - Status of cofee machine
  curl -X GET \
    http://localhost:8080/status \
    -H 'Cache-Control: no-cache' \
    -H 'Postman-Token: 72709ca5-09fb-45cc-bd3c-d692a0562c65'