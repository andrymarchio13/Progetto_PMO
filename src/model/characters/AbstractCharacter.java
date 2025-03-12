package model.characters;

import java.util.Random;

import model.equipment.potions.Potion;
import model.equipment.potions.PotionDefence;
import model.equipment.potions.PotionHealth;
import model.equipment.potions.PotionPower;
import model.equipment.weapons.Weapon;
import model.point.Point;
import model.equipment.potions.Potion;
public abstract class AbstractCharacter implements Character{
	
	private final int EXP_LEVELUP_THRESHOLD = 1000; //placeholder value
	private int maxHealth;
	private int currentHealth;
	private int speed;
	private int power;
	private int defence;
	private int experience;
	private Weapon weapon;
	private Potion potion;
	private Point position; 
	protected static final Random rand = new Random();
	
	public AbstractCharacter(int health, int speed, int power, int defence) {
		this.maxHealth = health;
		this.currentHealth = this.maxHealth;
		this.speed = speed;
		this.power = power;
		this.defence = defence;
		this.experience = 0;
		this.weapon = null;
		this.potion = null;
		this.position = new Point(0,0);
	}

	@Override
	public void moveTo(Point point) {
		// TODO Auto-generated method stub
	}
	
	public void setPosition(Point point) {
		this.position = point;
	}

	@Override
	public Point getPosition() {
		return this.position;
		
	}

	@Override
	public int getDistance(Point point) {
		return Math.abs(point.getX() - this.position.getX()) + Math.abs(point.getY() - this.position.getY()); //note that a diagonal spot is counted as two squares away, which is fine
	}

	@Override
	public void reduceHealth(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void increaseHealth(int value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void usePotion() {
		if(potion instanceof PotionHealth) {
			this.currentHealth += this.maxHealth * (potion.getHealtIncreased() / 100.0);
			
		}else if(potion instanceof PotionDefence) {
			this.defence += potion.getDefenceIncreased();
			
		}else if(potion instanceof PotionPower) {
			this.power += potion.getPowerIncreased();
			
		}else {
			this.speed += potion.getSpeedIncreased();
		}
	}

	@Override
	public Potion getPotion() {
		return this.potion;
	}

	@Override
	public void levelUp() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isAlive() {
		return this.currentHealth > 0;
	}

	@Override
	public int getExperience() {
		return this.experience;
	}

	@Override
	public void gainExperience(int value) {
		this.experience += value;
		if(this.experience >= this.EXP_LEVELUP_THRESHOLD) {
			this.levelUp();
			this.experience -= this.EXP_LEVELUP_THRESHOLD;
		}
	}

	@Override
	public int getHealth() {
		return this.currentHealth;
	}

	@Override
	public void setHealth(int health) {
		this.currentHealth = health;
	}

	@Override
	public int getSpeed() {
		return this.speed + this.weapon.getSpeed();
	}

	@Override
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public int getPower() {
		return this.power + this.weapon.getPower();
	}

	@Override
	public void setPower(int power) {
		this.power = power;
	}

	@Override
	public int getDefence() {
		return this.defence + this.weapon.getDefence();
	}

	@Override
	public void setDefence(int defence) {
		this.defence = defence;
	}

	@Override
	public Weapon getWeapon() {
		return this.weapon;
	}

	@Override
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
}
