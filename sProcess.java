public class sProcess {
  public int cputime;
  public int ioblocking;
  public int cpudone;
  public int ionext;
  public int numblocked;
  public int remainFromSlice;
  public int id;
  public static int totalid = 0;

  public sProcess (int cputime, int ioblocking, int cpudone, int ionext, int numblocked) {
	    this.cputime = cputime;
	    this.ioblocking = ioblocking;
	    this.cpudone = cpudone;
	    this.ionext = ionext;
	    this.numblocked = numblocked;
	    this.id = totalid++;
	  } 
  
  public String toString() {
	  return "{id: " + this.id + ", cputime: " + this.cputime  + 
			  ", ioblocking: " + this.ioblocking + 
			  ", cpudone: " + this.cpudone + ", next: " +
			  this.ionext + ", numblocked: " + this.numblocked + 
			  ", remainFromSlice: " + this.remainFromSlice  + "}"; 
  }
}
