import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class MinotaurBDay {
	public static void main(String[] args) throws InterruptedException{

		// 10 guests total
		Guest[] guestList = new Guest[10];

		// guest at index 0 is the counter, meaning that he is the only one counting how many cupcakes have been
		// replaced, he is also the only guests that will replace the cupcakes
		guestList[0] = new Guest(true, 0);
		guestList[1] = new Guest(false, 1);
		guestList[2] = new Guest(false, 2);
		guestList[3] = new Guest(false, 3);
		guestList[4] = new Guest(false, 4);
		guestList[5] = new Guest(false, 5);
		guestList[6] = new Guest(false, 6);
		guestList[7] = new Guest(false, 7);
		guestList[8] = new Guest(false, 8);
		guestList[9] = new Guest(false, 9);

		// starts threads
		for(int o = 0; o < 10; o++){
			guestList[o].start();
		}

		Random random = new Random();
		

		boolean allGuestsHaveEntered = false;

		while(!allGuestsHaveEntered){
			Guest.id = random.nextInt(10);
			//Guest.enterLabyrinth(guestList[n]);

			// when the counter has replaced the cupcake 10 times, he tells the minotaur to end the game
			if(guestList[0].count == 10)
			{
				allGuestsHaveEntered = true;
				Guest.gameIsRunning = false;
			}
		}
		
		for(int i = 0; i < 10; i++){
			guestList[i].join();
		}

		System.out.println("All guests have entered at least once, the game is over!");
	}
}

class Guest extends Thread{

	static volatile int id = 999999;
	int myId;
	boolean isCounter;
	boolean ateCupCake = false;
	boolean enteredLabyrinth = false;
	static volatile boolean gameIsRunning = true;
	int count = 0;
	static boolean cupcake = true;
	private static final ReentrantLock lock = new ReentrantLock();

	@Override
	public void run(){
		
		while(gameIsRunning){
			lock.lock();

				// if random number picked is the id of the threas, it enters the labyrinth
				if(id == myId)
				{
					// guest checks if there is a cupcake
					if(cupcake){
						if(ateCupCake == false){
							cupcake = false;
							ateCupCake = true;
							System.out.println("Cupcake eaten by thread " + myId);
							
							if(isCounter){
								cupcake = true;
								updateCount();
								System.out.println("Cupcake replaced by thread " + myId);
							}
						}
						else{
							System.out.println("Cupcake was there but not eaten by thread " + myId);
						}
					}
					else{
						System.out.println("Cupcake was not there so nothing was eaten by thread " + myId);
						if(isCounter){
							cupcake = true;
							updateCount();
							System.out.println("Cupcake replaced by thread " + myId);
						}
					}
			
					enteredLabyrinth = true;
				}
			lock.unlock();
		}

	}
		

	public void eatCupCake(){
		ateCupCake = true;
	}

	public Guest(boolean isCounter, int myId){
		this.isCounter = isCounter;
		this.myId = myId;
	}

	public void updateCount(){
		count++;
	}

}
