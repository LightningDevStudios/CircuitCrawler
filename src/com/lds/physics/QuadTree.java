package com.lds.physics;

import com.lds.math.Vector2;

import java.util.ArrayList;

/**
 * A Quadtree class designed for collision detection
 * @author Devin Reed
 * \todo: rename to Quadtree
 */
public class QuadTree 
{
    /*************
     * Constants *
     *************/
    
    /**
     * The maximum number of times a quadtree can divide
     */
    public static final int maxLevel = 2;
    
    /***********
     * Members *
     ***********/
	
    /**
     * All shapes contained partially or wholly within the Quadtre
     */
	private ArrayList<Shape> shapes;
	
	/**
	 * A list of all branches of the Quadtree. Should contain four or be null.
	 * Order is: Top Left, Top Right, Bottom Right, Bottom Left
	 */
	private QuadTree[] branches;
	
	/**
	 * Stores the parent Quadtree
	 * \todo: get rid of or use
	 */
	private QuadTree parent;
	
	/**
	 * A Vector2 representing the world coordinates of the Quadtree's bottom left corner
	 */
	private Vector2 position;
	
	/**
	 * A Vector2 representing the size of the Quadtree, in world coordinates
	 */
	private Vector2 size;
	
	/**
	 * The subdivision level of the tree. Root tree is zero, lowest tree possible is maxLevel
	 */
	private int level;
	
	/****************
	 * Constructors *
	 ****************/
	
	/**
	 * Initializes a new root instance of the Quadtree Class
	 * @param position The top left coordinate of the Quadtree, in world coords
	 * @param size The size of the Quadtree, in world coords
	 * @param shapes The list of shapes to be dealt with (all shapes in world)
	 */
	public QuadTree(Vector2 position, Vector2 size, ArrayList<Shape> shapes)
	{
	    this.position = position;
	    this.size = size;
	    this.shapes = shapes;
	    
	    parent = null;
	    level = 0;
	}
	
	/**
	 * Initializes a new branch instance of the Quadtree class
	 * @param parent The parent Quadtree (should be this)
	 * @param position The top left coordinate of the Quadtree, in world coords
	 * @param size The size of the Quadtree, in world coords
	 */
	private QuadTree(QuadTree parent, Vector2 position, Vector2 size)
	{
		this.parent = parent;
		this.position = position;
		this.size = size;
		
		level = this.parent.level + 1;
		
		shapes = new ArrayList<Shape>();
	}
	
	/******************
	 * Public Methods *
	 ******************/
	
	/**
	 * Updates the Quadtree
	 * <ol>
	 *     <li>Returns if Quadtree is at lowest level</li>
	 *     <li>If neccessary, branches out down the tree</li>
	 *     <li>Updates branches</li>
	 *     <li>Prunes up the tree</li>
	 * </ol> 
	 */
	public void update()
	{
	    //if at the lowest subdivision, do nothing
	    if (level == maxLevel)
	        return;
	    
	    //BRANCH: if there is more than one shape in the Quadtree
	    if (shapes.size() > 1)
	    {
	        //split into four branches if neccessary
	        if (branches == null)
	            split();
	        
	        //initialize a bounding box
	        AABB bb = new AABB();
	        //for each shape
	        for (Shape s : shapes)
	        {
	            //generate bounding box for the shape
	            bb.generateBounds(s.getWorldVertices());
	            
	            //for each branch
	            for (QuadTree branch : branches)
	            {
	                if (!branch.shapes.contains(s))
	                {
    	                float halfSizeX = branch.size.x() / 2;
    	                float halfSizeY = branch.size.y() / 2;
    	                //if shape's bounding box is contained in the branch
    	                if (bb.getLeftBound() >= branch.position.x() + halfSizeX) continue;
    	                if (bb.getRightBound() <= branch.position.x() - halfSizeX) continue;
    	                if (bb.getTopBound() <= branch.position.y() - halfSizeY) continue;
    	                if (bb.getBottomBound() >= branch.position.y() + halfSizeY) continue;
    	                    //add the shape to the branch
    	                    branch.addShape(s);
	                }
	            }
	        }
	        
	        //update all branches
	        for (QuadTree branch : branches)
	            branch.update();
	    }
	    
	    //PRUNE: if there is one or no shapes in the Quadtree
	    else
	    {
	        //clear all lower branches (unsplit)
	        branches = null;
	    }
	}
	
	/**
	 * Recursively gets all combinations of entities likely to be colliding
	 * @return An ArrayList of CollisionPairs: one pair for each two entities in the same lowest-level tree
	 */
	public ArrayList<CollisionPair> getCollisionPairs()
	{
	    //initialize a new list of collision pairs
	    ArrayList<CollisionPair> pairs = new ArrayList<CollisionPair>();
	    
	    //if at the lowest level
	    if (level == maxLevel)
	    {
	        //if two shapes, add the twp
	        if (shapes.size() == 2)
	        {
	            pairs.add(new CollisionPair(shapes.get(0), shapes.get(1)));
	        }
	        //TODO: more efficient linking
	        //if more than two, add all the combinations (if less than two, leave list blank)
	        else if (shapes.size() > 2)
	        {
	            for (int i = 0; i < shapes.size() - 1; i++)
	            {
	                for (int j = i + 1; j < shapes.size(); j++)
	                {
	                    pairs.add(new CollisionPair(shapes.get(i), shapes.get(j)));
	                }
	            }
	        }
	    }
	    //if not at lowest level, and is split
	    else if (branches != null)
	    {
	        //get collision pairs for all branches and add them together
	        for (QuadTree branch : branches)
	        {
	            ArrayList<CollisionPair> cps = branch.getCollisionPairs();
	            for (CollisionPair cp : cps)
	                pairs.add(cp);
	        }
	    }
	    
	    return pairs;
	}
	
	/**
	 * Adds an ArrayList of new shapes to shapes
	 * @param newShapes
	 */
	public void addShapes(ArrayList<Shape> newShapes)
    {
        shapes.addAll(newShapes);
    }
	
	/*******************
	 * Private Methods *
	 *******************/
	
	/**
	 * Splits the quadtree into four of 1/4 the size
	 */
	private void split()
	{
	    branches = new QuadTree[4];
	    
	    Vector2 branchSize = Vector2.scale(size, 0.5f);
	    Vector2 fourthSize = Vector2.scale(size, 0.25f);
	    
	    //Top Right
	    branches[0] = new QuadTree(this, Vector2.add(position, fourthSize), branchSize);
	    
	    //Top Left
	    branches[1] = new QuadTree(this, Vector2.add(position, new Vector2(-fourthSize.x(), fourthSize.y())), branchSize);
	    
	    //Bottom Left
	    branches[2] = new QuadTree(this, Vector2.subtract(position, fourthSize), branchSize);
	    
	    //Bottom Right
	    branches[3] = new QuadTree(this, Vector2.add(position, new Vector2(fourthSize.x(), -fourthSize.y())), branchSize);
	}
	
	/**
	 * Adds a new shape to shapes
	 * @param shape The new Shape to add
	 */
	private void addShape(Shape shape)
    {
        shapes.add(shape);
    }
}
