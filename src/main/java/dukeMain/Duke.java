package dukeMain;

import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Paths;

import dukeMain.commands.Command;
import dukeMain.exceptions.*;
import dukeMain.tasks.*;

public class Duke {
    private static ArrayList<Task> taskListing = new ArrayList<>();
    private static int count = 0;
    private final static String lineBreak = "---------------------------------------------------------";
    private Ui ui;
    private Storage storage;
    private TaskList tasks;
//    private dukeMain.Ui ui;

    public Duke(String filePath) {
        ui = new Ui();
        try {
            storage = new Storage(filePath);
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        } catch (InvalidStorageFilePathException e){
            ui.showInvalidStorageError();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }
    public static void main(String[] args) {
        new Duke("data/duke.txt").run();
    }
//        public static void main(String[] args) {
//        String logo = "  _       _ _      _ _______________________________________ \n"
//                    + " | |  _  | | |    | |  __  |        |  __  |  __  |___   ___|\n"
//                    + " | | | | | | |----| | |__| |  ------| |__| | |__|/    | |    \n"
//                    + " | |_| |_| | |----| | |  | |------  | |  | | |__|\\ ___| |___\n"
//                    + " |_________|_|    |_|_|  |_|________|_|  |_|______|_________|\n";
//        printThis("Hello from\n" + logo , 0);
//        printThis("What can do for you?",0);
//        Ui ui = new Ui();
//        taskListing.add(new ToDos("buy detergent"));
//        taskListing.add(new ToDos("buy Shampoo"));
//        taskListing.add(new Event("Keith Birthday","04.10.2021"));
//        taskListing.add(new Deadline("pay bills","23.11.2021"));
//        Scanner in;
//        String line;
//        while(true){
//            in = new Scanner(System.in);
//            line = in.nextLine();
//            if(line.equalsIgnoreCase("bye")) {
//                printThis("Bye. Hope to see you again soon!",0);
//                break;
//            }else if(line.equalsIgnoreCase("list")){
//                printList();
//            }
//            else if(line.equalsIgnoreCase("save")){
//                saveTask() ;
//            }
//            else{
//                try{
//                    addDeleteMarkTask(line);
//                }catch (ToDosBodyException e){
//                    printThis("OOPS!!! The description of a todo cannot be empty",3);
//                }catch (DeadlineBodyException e){
//                    printThis("OOPS!!! The description of a dukeMain.tasks.Deadline cannot be empty",3);
//                }catch (DeadlineByException e){
//                    printThis("OOPS!!! When is the dukeMain.tasks.Deadline ?",3);
//                }catch (EventBodyException e){
//                    printThis("OOPS!!! The description of a dukeMain.tasks.Event cannot be empty",3);
//                }catch (EventAtException e){
//                    printThis("OOPS!!! When is the dukeMain.tasks.Event ?",3);
//                }catch (InvalidCommandException e){
//                    printThis("OOPS!!! Invalid command please enter. Please try again",3);
//                }catch (DeleteBodyException e){
//                    printThis("OOPS!!! The description of a Delete cannot be empty",3);
//                }catch (NumberFormatException e){
//                    printThis("OOPS!!! Please enter a valid number",3);
//                }catch (IndexOutOfBoundsException e){
//                    printThis("OOPS!!! Please select between 1 to " + taskListing.size(),3);
//                }
//            }
//        }
//    }


//    public static void printList() {
//        int c = 1;
//        printThis(lineBreak,0);
//        for (Task tk : taskListing) {
//            printThis(c+") "+tk.toString(),0);
//            c ++;
//        }
//        printThis(lineBreak,0);
//    }

//    public static void markTask(String str){
//        String[] splited = str.split("\\s+");
//        if(splited.length == 2){
//            if (splited[0].equalsIgnoreCase("done")) {
//                int num;
//                try {
//                    num = Integer.parseInt(splited[1]) - 1;
//                } catch (NumberFormatException nfe) {
//                    printThis("Invalid number, Kindly enter 'List' to re-access",3);
//                    return;
//                }
//
//                if(num >= 0 && num < taskListing.size() ){
//                    printThis(lineBreak,0);
//                    printThis("Nice! I've marked this task as done:",0);
//                    taskListing.get(num).setDone();
//                    printThis(taskListing.get(num).toString(),0);
//                    printThis(lineBreak,0);
//                }else{
//                    printThis("Invalid Number",3);
//                }
//            }else{
//                printThis("Invalid command please enter 'List' again",3);
//            }
//        }
//    }

//    public static void addDeleteMarkTask(String item)
//            throws ToDosBodyException, DeadlineBodyException, DeadlineByException,
//            EventAtException, EventBodyException, InvalidCommandException, DeleteBodyException {
//        String[] split_1 = item.split("\\s+");
//        String[] split_2;
//        boolean isAdded = false;
//        boolean isRemoved = false;
//        boolean isDone = false;
//        if(split_1[0].equalsIgnoreCase("Todo")){
//            if(split_1.length == 1) throw new ToDosBodyException();
//            taskListing.add(new ToDos(split_1[1]));
//            isAdded = true;
//        }else if(split_1[0].equalsIgnoreCase("Deadline")){
//            if(split_1.length == 1)throw new DeadlineBodyException();
//            split_2 = item.split("/");
//            if(split_2.length == 1) throw new DeadlineByException();
//            taskListing.add(new Deadline(split_1[1],split_2[1]));
//
//        }else if(split_1[0].equalsIgnoreCase("Event")){
//            if(split_1.length == 1) throw new EventBodyException();
//            split_2 = item.split("/");
//            if(split_2.length == 1) throw new EventAtException();
//            taskListing.add(new Event(split_1[1],split_2[1]));
//        }else if(split_1[0].equalsIgnoreCase("delete")){
//            if(split_1.length == 1) throw new DeleteBodyException();
//            removeTask(Integer.parseInt(split_1[1]));
//            isRemoved = true;
//        }else if(split_1[0].equalsIgnoreCase("done")){
//            markTask(item);
//            isDone = true;
//        }
//        if (!isAdded && !isRemoved && !isDone) throw new InvalidCommandException();
//        if(isAdded){
//            printThis("Got it. I've added this task: " , 1);
//            printThis(taskListing.get(taskListing.size()-1).toString(), 0);
//            printThis("Now you have "+ taskListing.size()+ " dukeMain.tasks in the list." , 2);
//        }
//    }

//    public static void removeTask(int num) {
//        if(num > taskListing.size()) throw new IndexOutOfBoundsException();
//        printThis("Noted. I've removed this task:" , 1);
//        printThis(taskListing.get(num-1).toString(), 0);
//        printThis("Now you have "+ taskListing.size()+ " dukeMain.tasks in the list." , 2);
//        taskListing.remove(num-1);
//    }

    // 1 = Linebreak above
    // 2 = Linebreak below
    // 3 = Linebreak above and below
    // 0 = no linebreak
//    public static void printThis(String st, int style){
//        if(style == 1) System.out.println(lineBreak+"\n"+st);
//        else if(style == 2) System.out.println(st+"\n"+lineBreak);
//        else if(style == 3) System.out.println(lineBreak+"\n"+st+"\n"+lineBreak);
//        else System.out.println(st);
//    }
}