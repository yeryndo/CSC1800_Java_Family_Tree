import java.util.ArrayList;
import java.util.*;

public class Person {
    private String name;
    private Person parent1;
    private Person parent2;
    private ArrayList<String> children;

    //Making Person
    public Person(String name) {
        this.name = name;
        this.parent1 = null;
        this.parent2 = null;
        this.children = new ArrayList<>();
    }

    //Making Child
    public Person(String name, Person firstParent, Person secondParent) {
        this.name = name;
        this.parent1 = firstParent;
        this.parent2 = secondParent;
        this.children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Person getParent1() {
        return parent1;
    }

    public Person getParent2() {
        return parent2;
    }

    public void gaveBirth(Person infant) {
        this.children.add(infant.getName());
    }

    public ArrayList<String> getChildren() {
        return children;
    }

    public ArrayList<String> getSiblings(){
        ArrayList<String> siblings = new ArrayList<>();
        if (this.getParent1() != null){
            siblings.addAll(this.getParent1().getChildren());
        }
        if (this.getParent2() != null){
            for(String s : this.getParent2().getChildren()){ //in all parent2's children who will be my full or half siblings
                if(!siblings.contains(s)){
                    siblings.add(s);
                }
            }
        }
        siblings.remove(this.getName()); //remove yourself from the list of children
        return siblings;
    }

    public ArrayList<String> getAncestors(){
        ArrayList<String> ancestors = new ArrayList<>();
        if(parent1 == null && parent2 == null){ //no parents
            return ancestors; //has to stay null for other methods
        } else if(parent1 == null){
            ancestors.add(parent2.getName());
            ancestors.addAll(parent2.getAncestors());//recursive
        } else if(parent2 == null){
            ancestors.add(parent1.getName());
            ancestors.addAll(parent1.getAncestors());
        } else {
            ancestors.add(parent1.getName());
            ancestors.add(parent2.getName());
            ancestors.addAll(parent1.getAncestors());
            ancestors.addAll(parent2.getAncestors());
        }
        return ancestors;
    }

    public TreeMap<String, Integer> getCousins(Squad squad) { //work on
        TreeMap<String, Integer> cousin = new TreeMap<>(); //return list. key = person value = degree

        ArrayList<String> notAncestors = new ArrayList<>(); //called Person's not ancestors
        notAncestors.addAll(squad.getSquad().keySet());

        ArrayList<String> thisAncestors = this.getAncestors();
        notAncestors.removeAll(this.getAncestors());

        notAncestors.remove(this.getName());
        notAncestors.removeAll(this.getChildren());

        for (String stranger : notAncestors) {
            ArrayList<String> strangerAncestors = squad.getSquad().get(stranger).getAncestors();

            //look for common ancestor
            for (String strangerAncestor : strangerAncestors) {
                if (thisAncestors.contains(strangerAncestor)) {
//                    System.out.println("For strangerAncestor: " + strangerAncestor);
                    int thisIndex = thisAncestors.indexOf(strangerAncestor) / 2;
                    int strangerIndex = strangerAncestors.indexOf(strangerAncestor) / 2;
                    cousin.putIfAbsent(squad.getPerson(stranger).getName(), Math.min(thisIndex, strangerIndex));
//                    System.out.println("thisIndex " + thisIndex);
//                    System.out.println("strangerIndex: " + strangerIndex);
                }
            }
        }
        return cousin;
    }

    public TreeMap<String, Integer> getCousins(Squad squad, int degree) {
        TreeMap<String, Integer> cousin = new TreeMap<>();

        ArrayList<String> notAncestors = new ArrayList<>(); //input every Person; everyone
        notAncestors.addAll(squad.getSquad().keySet());

        ArrayList<String> thisAncestors = this.getAncestors(); //this person's ancestors
        notAncestors.removeAll(thisAncestors); //cannot be direct ancestor
        notAncestors.remove(this.getName()); //remove yourself
        notAncestors.removeAll(this.getChildren());

        for (String stranger : notAncestors) {
            ArrayList<String> strangerAncestors = squad.getSquad().get(stranger).getAncestors();
            //look for common ancestor
            for (String strangerAncestor : strangerAncestors) {
                if (thisAncestors.contains(strangerAncestor)) {
                    int thisIndex = thisAncestors.indexOf(strangerAncestor) / 2;
                    int strangerIndex = strangerAncestors.indexOf(strangerAncestor) / 2;
                    cousin.putIfAbsent(squad.getPerson(stranger).getName(), Math.min(thisIndex, strangerIndex));
                }
            }
        }

        Iterator<Map.Entry<String, Integer>> it = cousin.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            if (entry.getValue() != degree) {
                it.remove();
            }
        }
        return cousin;
    }

    public boolean isChild(Person target){
        if(parent1 == null || parent2 == null){
            return false;
        }
        if(parent1.equals(target) || parent2.equals(target)) {
            return true;
        } else{
            return false;
        }
    }

    public boolean isSibling(Person target){
        if(this.equals(target)){
            return false;
        } else if (parent1 == null || parent2 == null){
            return false;
        } else if(parent1 != null && (parent1.equals(target.getParent1()) || (parent1.equals(target.getParent2())))){
            return true;
        } else if (parent2 != null && (parent2.equals(target.getParent1()) || (parent2.equals(target.getParent2())))){
            return true;
        } else{
            return false;
        }
    }

    public boolean isAncestor(Person target){
        if(target.getAncestors().contains(this.name)){
            return true;
        }else {
            return false;
        }
    }

    public boolean isCousin(Person p2){
        Person p1 = this;
        if(this.equals(p2)){
            return false;
        } else if(this.isChild(p2) || p2.isChild(p1)){ //parents
            return false;
        } else {
            ArrayList<String> ancestors1 = this.getAncestors();
            ArrayList<String> ancestors2 = p2.getAncestors();

            if(ancestors1.contains(p2.getName()) || ancestors2.contains(p1.getName())){ //direct ancestors
                return false;
            }

            for (String anc : ancestors1) {
                if (ancestors2.contains(anc)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isUnrelated(Person p2){
        Person p1 = this;
        if(this.equals(p2)){ //not related to yourself
            return false;
        } else if(this.isChild(p2) || p2.isChild(p1)){
            return false;
        } else if(this.isSibling(p2)){
            return false;
        } else if(this.isCousin(p2)) {
            return false;
        } else if(this.isAncestor(p2) || p2.isAncestor(p1)){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", parent1=" + parent1 +
                ", parent2=" + parent2 +
                ", children=" + children +
                '}';
    }
}

