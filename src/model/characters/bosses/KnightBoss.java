package model.characters.bosses;

import model.characters.AbstractCharacter;
import model.characters.Knight;
import model.point.Point;

public class KnightBoss extends Knight{

	public KnightBoss(Point startingPosition) {
		super(startingPosition, "images/characters/knights/knightsBoss.png"); //Random
		this.increaseMaxHealth(0.3);
		this.increasePower(0.3);
		this.increaseDefence(0.3);
		this.increaseSpeed(0.3);
	}

	@Override
	public void setWeapon() {
	}
}
