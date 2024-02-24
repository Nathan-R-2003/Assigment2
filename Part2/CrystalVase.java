import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class CrystalVase{

	public static void main(String[] args) throws InterruptedException{

		Guest guestList[] = new Guest[10];
		
		guestList[0] = new Guest(0);
		guestList[1] = new Guest(1);
		guestList[2] = new Guest(2);
		guestList[3] = new Guest(3);
		guestList[4] = new Guest(4);
		guestList[5] = new Guest(5);
		guestList[6] = new Guest(6);
		guestList[7] = new Guest(7);
		guestList[8] = new Guest(8);
		guestList[9] = new Guest(9);

		for(int i = 0; i < 10; i++){
			guestList[i].start();
		}

		for(int i = 0; i < 10; i++){
			guestList[i].join();
		}

		System.out.println("All guests have visited the vase!");

	}
}

class Guest extends Thread{
	 static boolean busy = false;
	 static volatile boolean keepGoing = true;
	 private static final ReentrantLock lock = new ReentrantLock();
	 boolean visitedVase = false;
	 static volatile int count = 0;
	 int id;

	 @Override
	 public void run(){
		while(keepGoing){
			lock.lock();
			if(!busy && visitedVase == false){
				busy = true;
				visitedVase = true;
				count++;
				busy = false;

				System.out.println("Thread " + id + " visited the vase");

				if(count == 10) 
					keepGoing = false;
			} else if(busy && visitedVase == false)
				System.out.println("Thread " + id + " tried to visit the vase but room was busy");
			else if(visitedVase == true){
				System.out.println("Thread " + id + " already visited vase and decided not to do so again");
			}
			lock.unlock();
		}
	 }

	 public Guest(int id)
	 {
		this.id = id;
	 }
}