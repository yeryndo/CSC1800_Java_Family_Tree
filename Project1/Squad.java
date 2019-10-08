import java.util.*;

public class Squad {
    public HashMap<String, Person> squad;

    //making memory space or else it will throw null pointer exception
    public Squad() {
        this.squad = new HashMap<>();
    }

    public Person getPerson(String mapKey){
        return squad.get(mapKey);
    }

    public HashMap<String, Person> getSquad() {
        return squad;
    }

    public void setSquad(HashMap<String, Person> squad) {
        this.squad = squad;
    }

    public void joinSquad(Person member){
        this.squad.put(member.getName(), member);
    }

    public ArrayList<String> getUnrelated(Person target){
        ArrayList<String> strangers = new ArrayList<>();
        for(Person p : squad.values()){ //this method had to be outside of Person to use squad.values
            if(p.isUnrelated(target)){
                strangers.add(p.getName());
            }
        }
        Collections.sort(strangers);
        return strangers;
    }
}
