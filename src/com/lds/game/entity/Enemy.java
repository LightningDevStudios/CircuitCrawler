package com.lds.game.entity;

import com.lds.EntityManager;
import com.lds.game.SoundPlayer;
import com.lds.Enums.AIType;
import com.lds.Stopwatch;
import com.lds.game.ai.NodePath;
import com.lds.game.ai.Node;

public  abstract class Enemy extends Character //enemies will fall under this class
{
	public static int OUTER_RADIUS = 200, INNER_RADIUS = 75;
	private static int enemyCount = 0;
	protected AIType type;
	protected boolean agressive, colliding;
	protected int lastTime, randomTime;
	//Patrol stuff
	protected NodePath patrolPath;
	protected int patrolPathLocation;
	protected boolean onPatrol;
	//Pathfinding stuff
	protected NodePath pathToPlayer;
	protected int playerPathLocation;

	public Enemy(float size, float xPos, float yPos, boolean circular, int health, AIType type)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, circular, health, type);
	}
	
	public Enemy(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean circular, int health, AIType type)
	{
		super(size, xPos, yPos, angle, xScl, yScl, circular, health, 75.0f, 360.0f);
		this.type = type;
		lastTime = Stopwatch.elapsedTimeMs();
		randomTime = 500;
		enemyCount++;
		agressive = false;
		onPatrol = false;
		colliding = false;
	}
	
	@Override
	public void update()
	{
		super.update();
	}
	
	@Override
	public void die()
	{
		enemyCount--;
		SoundPlayer.getInstance().playSound(3);
		if ((int)(Math.random() * 2) == 1)
		{
			PickupEnergy pe = new PickupEnergy((int)(Math.random() * 75) + 25, this.getXPos(), this.getYPos());
			EntityManager.addEntity(pe);
		}
		else
		{
			PickupHealth ph = new PickupHealth((int)(Math.random() * 50) + 25, this.getXPos(), this.getYPos());
			EntityManager.addEntity(ph);
		}

		EntityManager.removeEntity(this);
	}
	
	/*@Override
	public void interact(Entity ent)
	{
		if (ent instanceof AttackBolt && !((AttackBolt)ent).doesIgnore(this))
		{
			takeDamage(25);
			agressive = true;
		}
	}*/
	
	//Patrol stuff
	
	public void setPatrolPath(NodePath patrolPath)
	{
		type = AIType.PATROL;
		this.patrolPath = patrolPath;
		patrolPathLocation = 0;
	}
	
	public NodePath getPatrolPath()
	{
		return patrolPath;
	}
	
	public Node getPatrolPathNode(int index)
	{
		return patrolPath.getNodeList().get(index);
	}
	
	public int getClosestPatrolPathNodeIndex()
	{
		return patrolPath.getNodeList().indexOf(patrolPath.getClosestNode(this));
	}
	
	public Node getClosestPatrolPathNode()
	{
		return patrolPath.getClosestNode(this);
	}
	
	public int getPatrolPathLocation()
	{
		return patrolPathLocation;
	}
	
	public boolean isOnPatrol()
	{
		return onPatrol;
	}
	
	public void setPatrolPathLocation(int patrolPathLocation)
	{
		this.patrolPathLocation = patrolPathLocation;
	}
	
	public void setOnPatrol(boolean onPatrol)
	{
		this.onPatrol = onPatrol;
	}
	
	//General Stuff
	
	public boolean isColliding()
	{
		return colliding;
	}
	
	public void setColliding(boolean colliding)
	{
		this.colliding = colliding;
	}
	
	public AIType getType()
	{
		return type;
	}
	
	public boolean isAgressive()
	{
		return agressive;
	}
	
	public static int getEnemyCount ()
	{
		return enemyCount;
	}
	
	public int getRandomTime()
	{
		return randomTime;
	}
	
	public int getLastTime()
	{
		return lastTime;
	}
	
	public void setAgressive(boolean agressive)
	{
		this.agressive = agressive;
	}
	
	public void setRandomTime(int randomTime)
	{
		this.randomTime = randomTime;
	}
	
	public void setLastTime(int lastTime)
	{
		this.lastTime = lastTime;
	}
	
	public void setPathToPlayer(NodePath np)
	{
		pathToPlayer = np;
	}
	
	public NodePath getPathToPlayer()
	{
		return pathToPlayer;
	}
	
	public int getPlayerPathLocation()
	{
		return playerPathLocation;
	}
	
	public void setPlayerPathLocation(int playerPathLocation)
	{
		this.playerPathLocation = playerPathLocation;
	}	
}
