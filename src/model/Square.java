package model;

public class Square {

	private int posX, posY;
	private boolean shooted;
	private Boat boat;
	
	public Square(int x, int y){
		posX = x;
		posY = y;
		shooted = false;
		
	}

	public boolean isShooted() {
		return shooted;
	}

	public void setShooted(boolean shooted) {
		this.shooted = shooted;
	}

	public Boat getBoat() {
		return boat;
	}

	public void setBoat(Boat boat) {
		this.boat = boat;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + posX;
		result = prime * result + posY;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Square other = (Square) obj;
		if (posX != other.posX)
			return false;
		if (posY != other.posY)
			return false;
		return true;
	}

	public boolean tirer(){
		
		shooted = true;
		if(boat != null){
			return(boat.toucher(this));
		}
		
		return false;
	}
	
}