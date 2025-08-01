package model.characters;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;

import model.equipment.potions.*;
import model.equipment.weapons.Axe;
import model.equipment.weapons.LongSword;
import model.equipment.weapons.Weapon;
import model.point.Point;
public abstract class AbstractCharacter implements Character, Serializable {
	public static final int EXP_LEVELUP_THRESHOLD = 1000; //threshold at which you level up each time. placeholder value
	public static final int SPEED_TO_MOVEMENT = 10;
	private int maxHealth;
	private int currentHealth;
	private int speed;
	private int power;
	private int defence;
	private int experience;
	private Weapon weapon;
	private Potion potion;
	private Point position; 
	private String imagePath; //filepath
	private Image image;
	private ImageIcon icon;
	private boolean isAllied;
	protected ArrayList<Weapon> availableWeapons;
	protected static final Random rand = new Random();
	private static final long serialVersionUID = 1L;  // per salvare lo stato del gioco

	public AbstractCharacter(int health, int speed, int power, int defence) {
		this.maxHealth = health;
		this.currentHealth = this.maxHealth;
		this.speed = speed;
		this.power = power;
		this.defence = defence;
		this.experience = 0;
		this.potion = null;
		this.isAllied = false;
		this.generateDefaultImage();
		this.availableWeapons = new ArrayList<>(2);
	}

	@Override
	public void moveTo(Point point) throws IllegalArgumentException {
		/*if(this.getDistanceInSquares(point) > (this.speed / AbstractCharacter.SPEED_TO_MOVEMENT)) //movement per-turn depends on speed, the actual value is placeholder as of now
			throw new IllegalArgumentException("You tried to move farther than your movement speed allows!"); */
		this.position = point;
		
	}

	//note that a diagonal spot is counted as two squares away, which is fine
	@Override
	public int getDistanceInSquares(Point point) {
		return this.position.distanceFrom(point);
	}

	@Override
	public void usePotion() {
		if (this.hasPotion()) {
			if(this.potion instanceof PotionHealth) {
				this.increaseCurrentHealth(this.potion.getPotionValue());

			}else if(potion instanceof PotionDefence) {
				this.increaseDefence(this.potion.getPotionValue());

			}else if(potion instanceof PotionPower) {
				this.increasePower(this.potion.getPotionValue());

			}else if(potion instanceof PotionSpeed){
				this.increaseSpeed(this.potion.getPotionValue());
			}
		}
	}

	private void levelUp() {
		double statIncreasePercentage = 0.10;  

		this.increaseMaxHealth(statIncreasePercentage);
		this.increasePower(statIncreasePercentage);
		this.increaseDefence(statIncreasePercentage);
		this.increaseSpeed(statIncreasePercentage);
		this.currentHealth = this.maxHealth; //levelling up fully heals you
	}

	@Override
	public void gainExperience(int value) {		
		this.experience += value;
		while(this.experience >= AbstractCharacter.EXP_LEVELUP_THRESHOLD) { //why While instead of if? this way, you can immediatly give, say, 5 thresholds worth of exp to a character and he levels up to 5 immediatly
			this.levelUp();
			this.experience -= AbstractCharacter.EXP_LEVELUP_THRESHOLD;
		}
	}

	@Override
	public void reduceCurrentHealth(int value) {
		if(value >= 0)
			this.currentHealth -= value;
	}

	@Override
	public void increaseCurrentHealth(double percentage) {
		if(percentage >= 0) {
			this.currentHealth += this.maxHealth * percentage;
			if(this.currentHealth > this.maxHealth) //current health must remain capped to the maximum health value
				this.currentHealth = this.maxHealth;
		}
	}

	//used only to create the playable characters basically
	@Override
	public void becomeHero() {
		double heroStatIncreasePercentage = 0.90;   //Per debug metter 2000

		if(!this.isAllied) {
			this.isAllied = true;
			this.increaseMaxHealth(heroStatIncreasePercentage);
			this.increasePower(heroStatIncreasePercentage);
			this.increaseDefence(heroStatIncreasePercentage);
			this.increaseSpeed(heroStatIncreasePercentage);
		
			this.setImagePath("images/characters/" + getClass().getSimpleName().toLowerCase() + "/" + getClass().getSimpleName().toLowerCase() + "Hero.png");
			this.icon = new ImageIcon(this.imagePath);
	        this.image = this.icon.getImage().getScaledInstance(75, 45, Image.SCALE_AREA_AVERAGING);
		}
	}
	
	@Override
	public void setPosition(Point position) {
		this.position = position;
	}

	//If someone dies, returns their Character, otherwise returns null
	@Override
	public Character fight(Character attackedCharacter) throws IllegalArgumentException {
		Character deadCharacter = null;
		
		if(this.isAllied() == attackedCharacter.isAllied())
			throw new IllegalArgumentException("You cannot attack someone belonging to your own faction!");
		if (!this.isWithinAttackRange(this, attackedCharacter))
			throw new IllegalArgumentException("You cannot attack someone outside of your weapon's attack range!");

		//use your potion IF: you have one, but NOT if it's an health potion and you're at max health (that would be useless)
		if(this.hasPotion() && !(this.getPotion() instanceof PotionHealth && this.getCurrentHealth() == this.getMaxHealth())) { 
			this.usePotion();
		}
		
		attackedCharacter.reduceCurrentHealth(this.getPower() - attackedCharacter.getDefence()); // start of combat

		if (attackedCharacter.isAlive() && this.isWithinAttackRange(attackedCharacter, this)) //if attacked character is still alive and its weapon can reach you, it counterattacks
			this.reduceCurrentHealth(attackedCharacter.getPower() - this.getDefence());

		if (!attackedCharacter.isAlive()) {
			deadCharacter = attackedCharacter;
			this.gainExperience(AbstractCharacter.EXP_LEVELUP_THRESHOLD/3);
			//random potion drop on kill
			switch(rand.nextInt(0,9)) { //50% chance of getting a potion, if so get one of the four randomly
			case 5:
				this.setPotion(new PotionHealth());
				break;
			case 6:
				this.setPotion(new PotionDefence());
				break;
			case 7:
				this.setPotion(new PotionPower());
				break;
			case 8:
				this.setPotion(new PotionSpeed());
				break;				
			}
		}

		if (!this.isAlive()) {
			deadCharacter = this;
			attackedCharacter.gainExperience(AbstractCharacter.EXP_LEVELUP_THRESHOLD/3);
		}
		
		return deadCharacter;
	}

	private boolean isWithinAttackRange(Character attackingCharacter, Character attackedCharacter) {
		return attackingCharacter.getDistanceInSquares(attackedCharacter.getPosition()) <= attackingCharacter.getRange();
	}

	protected void increaseMaxHealth(double percentage) {		
		this.maxHealth += this.maxHealth * percentage;
		this.increaseCurrentHealth(percentage);
	}

	protected void increasePower(double percentage) {
		this.power += this.power * percentage;
	}

	protected void increaseDefence(double percentage) {
		this.defence += this.defence * percentage;
	}

	protected void increaseSpeed(double percentage) {
		this.speed += this.speed * percentage;
	}

	protected void spawnWeapon() {
		this.setWeapon(this.availableWeapons.get(rand.nextInt(0,2)));
	}

	@Override
	public void swapWeapon() {
		if(this.availableWeapons.getFirst().equals(this.getWeapon()))
			this.setWeapon(this.availableWeapons.getLast());
		else
			this.setWeapon(this.availableWeapons.getFirst());
	}

	protected void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	@Override
	public void setPotion(Potion potion) {
		if(!this.hasPotion()) {
			this.potion = potion;
			System.out.println( this.getClass().getSimpleName() + " got a " + potion.getClass().getSimpleName() + " potion!");
		}
	}

	protected void generateDefaultImage() {
		this.setImagePath("images/characters/" + this.getClass().getSimpleName().toLowerCase() + "/" + this.getClass().getSimpleName().toLowerCase() + this.rand.nextInt(1,4) + ".png");
		this.icon = new ImageIcon(this.imagePath);
        this.image = this.icon.getImage().getScaledInstance(75, 45, Image.SCALE_AREA_AVERAGING); //Controls actual image size scaling on the map!!!
	}

	@Override
	public Point getPosition() {
		return this.position;
	}

	@Override
	public int getCurrentHealth() {
		return this.currentHealth;
	}

	@Override
	public int getMaxHealth() {
		return this.maxHealth;
	}

	@Override
	public int getSpeed() {
		return this.speed + this.weapon.getSpeed();
	}

	@Override
	public int getPower() {
		return this.power + this.weapon.getPower();
	}

	@Override
	public int getDefence() {
		return this.defence + this.weapon.getDefence();
	}

	@Override
	public int getRange() {
		return this.weapon.getRange();
	}

	@Override
	public int getExperience() {
		return this.experience;
	}

	@Override
	public Weapon getWeapon() {
		return this.weapon;
	}

	@Override
	public Potion getPotion() {
		return this.potion;
	}

	@Override
	public boolean hasPotion() {
		return this.potion != null;
	}

	@Override
	public String getImagePath() {
		return imagePath;
	}
	
	@Override
	public Image getImage() {
		return this.image;
	}

	public void setImagePath(String image) { //updates the icon-image too as there's no point to having them be different
		this.imagePath = image;
		//Pre-process images 
		ImageIcon icon = new ImageIcon(this.imagePath);
        this.image = icon.getImage().getScaledInstance(75, 45, Image.SCALE_AREA_AVERAGING); //Controls actual image size scaling on the map!!!
	}

	@Override
	public boolean isAllied() {
		return isAllied;
	}

	@Override
	public boolean isAlive() {
		return this.currentHealth > 0;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": [maxHealth="+ maxHealth + ", currentHealth=" + currentHealth + ", speed=" + speed
				+ ", power=" + power + ", defence=" + defence + ", experience=" + experience + ", weapon=" + weapon.getClass().getSimpleName()
				+ ", potion=" + potion + ", position=" + position + ", isAllied=" + isAllied + "]";
	}
}
