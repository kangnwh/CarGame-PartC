package mycontroller;

public class NextPositionFactory {



    public static INextPositionStrategy chooseNextPositionStrategy(MyAIController myAIController){
        if(myAIController.getHealth()<30){
            return new HealPositionStrategy();
        }
        else if(myAIController.getKey()!=1&&myAIController.getMapRecorder().keyFounded(myAIController.getKey()-1)){
            return new KeyPositionStrategy();
        }
        else if(myAIController.getKey()==1&&myAIController.getMapRecorder().isExitFounded()){
            return new ExitPositionStrategy();
        }
        else{
            return new ExplorePositionStragegy();
        }
    }
}
