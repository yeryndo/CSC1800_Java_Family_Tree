import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String query = "";
        try{
            query = in.readLine();
        } catch(IOException y){
            System.out.println(y);
        }
        Squad squad = new Squad();

        while(query != null){
            String[] lines = query.split("\n");
            for (String n : lines) {
                String[] line = n.split(" ");
                switch(line[0]){
                    case("E"):
                        System.out.println(n);
                        if(line.length > 4){
                            System.out.println("Not a valid query");
                        } else if(line.length == 2){
                            Person single = squad.getPerson(line[1]);
                            if(single == null){
                                single = new Person(line[1]);
                                squad.joinSquad(single);
                            }
                        }else{
                            if(line.length == 3){
                                    Person lover1 = squad.getPerson(line[1]);
                                    Person lover2 = squad.getPerson(line[2]);
                                    //if there is no person already created, they will be null. so make a new person and join
                                    if(lover1.equals(lover2)){
                                        System.out.println("You cannot marry yourself");
                                    } else{
                                        if(lover1 == null){
                                            lover1 = new Person(line[1]);
                                            squad.joinSquad(lover1);
                                        }
                                        if(lover2 == null){
                                            lover2 = new Person(line[2]);
                                            squad.joinSquad(lover2);
                                        }
                                        squad.joinSquad(lover1);
                                        squad.joinSquad(lover2);
                                    }
                                }
                            if(line.length == 4){ //input of 2 parents with 1 child
                                Person parent1 = squad.getPerson(line[1]);
                                Person parent2 = squad.getPerson(line[2]);
                                //if some new random person wants to come marry, getPerson will return null so check conditions and make adjustments
                                if(parent1 == null){
                                    parent1 = new Person(line[1]);
                                    squad.joinSquad(parent1);
                                }
                                if(parent2 == null){
                                    parent2 = new Person(line[2]);
                                    squad.joinSquad(parent2);
                                }
                                if(parent1.equals(parent2)){
                                    System.out.println("You cannot have a kid with yourself");
                                } else{
                                    if(parent1 == null){
                                        parent1 = new Person(line[1]);
                                        squad.joinSquad(parent1);
                                    }
                                    if(parent2 == null){
                                        parent2 = new Person(line[2]);
                                        squad.joinSquad(parent2);
                                    }
                                    //rest of family history of adding parent child relationship
                                    if(squad.getPerson(line[3]) != null){
                                        //System.out.println(squad.getPerson(line[3]));
                                        System.out.println("Someone already gave birth to " + line[3]);
                                    } else{ //Person did not exist. Newborn baby Person
                                        Person child = new Person(line[3], parent1, parent2);
                                        parent1.gaveBirth(child);
                                        parent2.gaveBirth(child);
                                        squad.joinSquad(child);
                                    }
                                    //System.out.println(child.toString());
                                }
                            }
                        }
                        System.out.println();
                        break;
                    case("X"): //is <> the <> of <>
                        System.out.println(n);
                        if (line.length < 4 || line.length > 5){
                            System.out.println("Not a valid query");
                            System.out.println();
                            break;
                        }
                        //get Person objects of each person input
                        Person p1 = squad.getPerson(line[1]);
                        Person p2 = squad.getPerson(line[3]);
                        if (line.length == 4) {
                            if(p1 == null){
                                System.out.println(line[1] +" does not exist");
                                System.out.println();
                                break;
                            }
                            if(p2 == null){
                                System.out.println(line[3]+" does not exist");
                                System.out.println();
                                break;
                            }
                            switch(line[2]){
                                case("child"):
                                    if(p1.isChild(p2)){
                                        System.out.println("Yes");
                                    } else {
                                        System.out.println("No");
                                    }
                                    break;
                                case("sibling"): //condition if compared with people who do not exist
                                    if(p1.isSibling(p2)){
                                        System.out.println("Yes");
                                    } else {
                                        System.out.println("No");
                                    }
                                    break;
                                case("ancestor"):
                                    if(p1.isAncestor(p2)){
                                        System.out.println("Yes");
                                    } else{
                                        System.out.println("No");
                                    }
                                    break;
                                case("cousin"):
                                    TreeMap<String, Integer> cousins = p1.getCousins(squad);
                                    if(cousins.containsKey(p2.getName())){
                                        System.out.println("Yes");
                                    }
                                    else{
                                        System.out.println("No");
                                    }
                                    break;
                                case("unrelated"):
                                    if(p1.isUnrelated(p2)){
                                        System.out.println("Yes");
                                    } else {
                                        System.out.println("No");
                                    }
                                    break;
                                default:
                                    System.out.println("I do not understand this relationship");
                                    break;
                            }
                            System.out.println();
                            break;

                        }
                        if (line.length == 5){ //check if A is the nth degree of B
                            String inputDegree = line[3];
                            int targetDegree = Integer.parseInt(inputDegree);
                            Person p3 = squad.getPerson(line[4]);

                            if(p1 == null){
                                System.out.println(line[1] +" does not exist");
                                System.out.println();
                                break;
                            }
                            if(p3 == null){
                                System.out.println(line[3]+" does not exist");
                                break;
                            }

                            if(p3 != null){
                                TreeMap<String, Integer> cousins = p1.getCousins(squad);
                                if(cousins.containsKey(p3.getName()) && (cousins.get(p3.getName()) == targetDegree)){
                                    System.out.println("Yes");
                                }
                                else{
                                    System.out.println("No");
                                }
                            } else {
                                System.out.println(line[4]+ "does not exist");
                                System.out.println();
                            }
                            System.out.println();
                            break;
                        }
                        System.out.println();
                        break;

                    case("W"): //list everyone who is a <> of <>
                        System.out.println(n);
                        if(line.length < 3 || line.length > 4){
                            System.out.println("Not a valid query");
                            System.out.println();
                            break;
                        }
                        Person target = squad.getPerson(line[2]);
                        if(line.length == 3){
                            if(target == null){
                                System.out.println("Who is " + line[2] + " that you are asking about?");
                                System.out.println();
                                break;
                            }
                            switch(line[1]){
                                case("child"):
                                    if(target.getChildren().size() > 0){
                                        Collections.sort(target.getChildren());
                                        for(String child: target.getChildren()){
                                            System.out.println(child);
                                        }
                                    } else{
                                        System.out.println("No children exist");
                                        System.out.println();
                                    }
                                    break;
                                case("sibling"):
                                    Collections.sort(target.getSiblings());
                                    if(target.getSiblings().size() > 0){
                                        for(String sibling: target.getSiblings()){
                                            System.out.println(sibling);
                                        }
                                    } else{
                                        System.out.println("No siblings exist");
                                    }
                                    break;
                                case("ancestor"):
                                    ArrayList<String> ancestors = target.getAncestors();
                                    ArrayList<String> nonDupList = new ArrayList<String>();

                                    for (String dupWord : ancestors){
                                        if(!nonDupList.contains(dupWord)){
                                            nonDupList.add(dupWord);
                                        }
                                    }
                                    Collections.sort(nonDupList);
                                    for(String s : nonDupList){
                                        System.out.println(s);
                                    }
                                    if(nonDupList.size() == 0){
                                        System.out.println("No ancestors exist");
                                    }
                                    break;
                                case("cousin"):
                                    TreeMap<String, Integer> cousins = target.getCousins(squad);
                                    cousins.forEach((person, degrees) -> {
                                        System.out.println(person);
                                    });

                                    if(cousins.size() == 0){
                                        System.out.println("No cousins exist");
                                    }
                                    break;
                                case("unrelated"):
                                    Collections.sort(squad.getUnrelated(target));
                                    if(squad.getUnrelated(target) == null){
                                        System.out.println("There are no one unrelated");
                                    } else{
                                        for(String unrelated : squad.getUnrelated(target)){
                                            System.out.println(unrelated);
                                        }
                                    }
                                    break;
                                default:
                                    System.out.println("I do not recognize your input");
                                    break;
                            }
                            System.out.println();
                            break;
                        }
                        if(line.length == 4){ //ex: W cousin 2 Mary; print everyone who is the second cousin of Mary
                            Person target1 = squad.getPerson(line[3]);
                            if(target1 == null){
                                System.out.println( line[3]+" does not exist");
                                System.out.println();
                                break;
                            }
                            String degreeString = line[2];
                            int degree = Integer.parseInt(degreeString);
                            Person cat = squad.getPerson(line[3]); // Person of interest if in squad

                            TreeMap<String, Integer> cousins = cat.getCousins(squad,degree);
                            //System.out.println("Entries in cousins: " + cousins.size());
                            if(degree == 0){
                                Collections.sort(target1.getSiblings());
                                for(String s : target1.getSiblings()){
                                    System.out.println(s);
                                }
                            }else{
                                cousins.forEach((person, degrees) -> {
                                    System.out.println(person);
                                });
                                if(cousins.isEmpty()){
                                    System.out.println("There are no cousins of degree " + degreeString + " for " + line[3]);
                                }
                            }

                        }
                        System.out.println();
                        break;
                }
            }
            try{
                query = in.readLine();
            } catch(IOException y) {
                System.out.println(y);
            }
        }
    }
}
