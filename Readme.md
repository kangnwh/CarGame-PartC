# Design Rationale

 

##  Introduction 

The overall requirement of our task is to implement a carauto controller to traverse the map and its traps, including exploring a map,locating the keys, retrieving the keys in order, and finding its way to exitwith sufficient health. This report aims to clarify and critically analysis ourdesign choices. In detail, this report will discuss the analysis phase anddesign decisions made by our team in the analysis phase. 

 

## Our task in the design

The project provided by our lecturer is provided with tilesin the map, utilities including Coordinate, peekTube, and world including car,world and world spatial. We are required to implement our own controller,enabling the car to find its way to exit with the collecting keys in order.   

 

## How we design our controller

The overall design of our controller has four classes andtwo strategy patterns. These class are Drive, carStatus, MapRecorder,MyAIController and OperationType, and two strategies are the strategies ofPathDiscovery and strategies of PositionStrategy. 

 

To be more specific, MyAIController is for updating the car(handling the operation).  Drive is aninformation expert that is to determine which way to go with the informationfrom map recorder and next position strategies. CarStatus is a class thatrecord status of the car, its angles, positions and health. MapRecorder is forrecording information from the map, and with these information, help the car tofind the path by referring different path strategies in different situations.The operationType is to list different operations, including break, forward,backward, turning north, turning south, turning east and turning west. 

 

As for the position strategies, we have exitPositionStrategy(handling the way out of the car), ExplorePositionStrategy (handling the carexploration), HealPositionStrategy (handling the situation of low HP of thecar), KeyPositionStrategy (handling the way to locate the key in order)

 

As for the PathDiscovery strategies, it has AStarStrategyand Node, providing the strategy for the map recorder in determining the pathfrom current position to target position. This strategy is implements by theinterface of IDiscoveryStrategy. At the moment, we only have one strategy forpath calculation, but in the future, it can be more than one path discoverystrategies in our system, so the interface is preparing for the future usage. 

 

## Why we made this decisionfor controller design

## Use strategy pattern withreferring to GoF

When we approach this task in our discussion, we noticedthat to find the way out, the car has several tasks to do. This first is toexplore the map to locate the keys. Second is to get the keys in order, andfinally is to make the ways out. In the whole process, the car has also tomaintain a sufficient HP.  In orderwords, the car has to make different strategies during the task. To solve thisproblem, we define each strategy in a separate class, with a common interface.

​            

Therefore, we came up with position strategies, handling anddetermining the car's next position according to different situations asmentioned above. With introducing the strategy pattern, it is easier to modifyeach strategy in separate class. It is also easier to extend our performance ifwe come more advanced strategies for the task.

​            

## Path strategy

At the beginning, we created a class called Path to handlewith the task of locating the way from one point to the other point. However,after discussion, we found that it was illogical that the Path class find thepath, and it was more logical for the map recorder to find a path with itssufficient information about the map. Therefore, we came up with a new ideathat the map recorder is to handle path discover, and path only providesstrategy for map recorder. 

To be more specific, Path strategy provided solution for the MapRecorder
class to determine the path from one point to another point, such as from car
current position to the key position or healing position. We believed that the
MapRecorder class is able to handle how the car find its way by using function
such as findPath, however, its decision can be different according to a
specific situation. Therefore, we came up with the path discovery strategies
pattern, handling these various situations. 

​            

## Use factory pattern** 

With many strategies in our design, we came up with an ideathat we can apply the factory strategy pattern in our design. The reason behindis that to create instance for each strategy is complicated and it’s hard tomanage when strategy classed are changed in the future. With the implementationof our factory pattern called NextPositionFactory, we are able to instantiatethe class that we want. 

​            

In addition, with the factory strategy, we are able toimplement our composite strategy pattern. In detail, in using the strategy suchas key position strategy, exit position strategy and explore position strategy,we also can apply heal position strategy as long as the HP lower than 30 andthe map has health trap.

​            

The NestPositionFactory is also a good way of decoupling, byinstantiating the strategies within a specific class.

​            

## The informationexpert** 

After analyzing the AIcontroller provided by our lecturer.We notice that the controller is not good enough to because it is only forassigning the task. So, we re-designed it, and introduced an information expertcalled Drive. The class has the information of the keys, exits, healing traps.Also, the class can also inform the our MyAIController for the operation. Thebenefit of inWith this information expert, the controller need to callhandleOperation only, and handle Operation will get the operation for theexpert. 

​            

Controller - only assign task - HighCohesion

As menttioned above, we need a better controller that onlyassign tasks. The AIController, however, do everything for the car, including exploringthe map and operations. This controller cannot meet the GRASP requriement ofhigh cohesion and low coupling. 

​            

So, we create a new controller called myAIController and overwaitedthe update functions that handled the operation function by referring the drivefor instruction. With the new controller, it only assigns tasks for theinformation expert - Drive. Therefore, it achieves high cohesion and lowcoupling.

​            

## Decoupling to achievehigh cohesion

With decoupling in mind, our group try to make differentclasses such that each class is high corelated. For example, the   carStatus class referring the car'sposition, health and angle, which created by myAIController, rather in withinthe controller class. Moreover, the class of OperationType only handle with thecar's turning, acceleration and break, which provides a high cohesion andreducing the coupling in the class of myAIController.

 

## Conclusion

All in all, our implementation is based on our modelling andpattern design, and our design is guided by GRASP patterns and GoF patterns. Insummary, we have strategy patterns for path discovery and position guidance.Factory pattern and composite strategy are also being applied in the positionstrategy. We have an information expert called drive, handling differentsituations, which assigned by myAIController. To achieve high cohesion and lowcoupling, we have operation type class handling various operations types, andwe have map recorder class, handling all kinds of map information about keys,map tiles, exit and path to a specific point.

  
## Contributors:
- Eric Kang
- Tony Gu
- Bok Mo
 

 

 