package Maze;

import java.util.*;

/**
 * Class that solves maze problems with backtracking.
 * @author Koffman and Wolfgang
 **/
public class Maze implements GridColors {

    /** The maze */
    private TwoDimGrid maze;

    public Maze(TwoDimGrid m) {
        maze = m;
    }

    /** Wrapper method. */
    public boolean findMazePath() {
        return findMazePath(0, 0); // (0, 0) is the start point.
    }

    /**
     * Attempts to find a path through point (x, y).
     * @pre Possible path cells are in BACKGROUND color;
     *      barrier cells are in ABNORMAL color.
     * @post If a path is found, all cells on it are set to the
     *       PATH color; all cells that were visited but are
     *       not on the path are in the TEMPORARY color.
     * @param x The x-coordinate of current point
     * @param y The y-coordinate of current point
     * @return If a path through (x, y) is found, true;
     *         otherwise, false
     */
    public boolean findMazePath(int x, int y) {
        //1. 如果节点是不能访问， 返回假
        if(!maze.getColor(x,y).equals(NON_BACKGROUND))
            return false;

        //到达出口节点， 并且因为1, 节点是可以访问的
        if(x == maze.getNCols() - 1 && y == maze.getNRows() - 1){
            maze.recolor(x, y, PATH);
            return true;
        }

        //设置当前节点为访问
        maze.recolor(x, y, TEMPORARY);
        //判断右节点， 如果找到了就返回。
        if(x + 1 < maze.getNCols() && findMazePath(x + 1, y)){
            maze.recolor(x, y, PATH);
            return true;
        }
        //判断下节点
        if(y + 1 < maze.getNRows() && findMazePath(x, y + 1)){
            maze.recolor(x, y, PATH);
            return true;
        }
        //判断左节点
        if(x - 1 >= 0 && findMazePath(x - 1, y)){
            maze.recolor(x, y, PATH);
            return true;
        }
        //判断上节点
        if(y - 1 >= 0 && findMazePath(x, y - 1)){
            maze.recolor(x, y, PATH);
            return true;
        }
        maze.recolor(x, y, NON_BACKGROUND);
        //上下左右没有找到适合的， 返回假
    	return false;
    }

    // ADD METHOD FOR PROBLEM 2 HERE
    // Not use stack, ArrayList
    public ArrayList<ArrayList<PairInt>> findAllMazePaths(int x, int y){
        ArrayList<ArrayList<PairInt>> result = new ArrayList<>();
        findMazePathBased(x, y, result, new ArrayList());
        return result;
    }

    public void findMazePathBased(int x, int y, ArrayList<ArrayList<PairInt>> result, ArrayList<PairInt> trace){
        if(!maze.getColor(x,y).equals(NON_BACKGROUND))
            return;
        if(x == maze.getNCols() - 1 && y == maze.getNRows() - 1){
            trace.add(new PairInt(x, y));
            result.add(new ArrayList(trace));
            trace.remove(trace.size() - 1);
            return;
        }
        PairInt pairInt = new PairInt(x, y);
        trace.add(pairInt);
        maze.recolor(x, y, TEMPORARY);
        if(x + 1 < maze.getNCols()){
            findMazePathBased(x + 1, y, result, trace);
        }

        if(y + 1 < maze.getNRows()){
            findMazePathBased(x, y + 1, result, trace);
        }

        if(x - 1 >= 0){
            findMazePathBased(x - 1, y, result, trace);
        }

        if(y - 1 >= 0) {
            findMazePathBased(x, y - 1, result, trace);
        }

        maze.recolor(x, y, NON_BACKGROUND);
        trace.remove(trace.size() - 1);
    }

    // ADD METHOD FOR PROBLEM 3 HERE
    public ArrayList<PairInt> findMazePathMin(int x, int y){
        return bfs();
    }
    public ArrayList<PairInt> bfs(){
        int col = maze.getNCols();
        int row = maze.getNRows();
        int[] trace = new int[row * col];
        Arrays.fill(trace, -1);
        trace[0] = 0;
        Queue<PairInt> queue = new LinkedList<PairInt>();
        if(!maze.getColor(0,0).equals(NON_BACKGROUND)){
            return new ArrayList<PairInt>();
        }
        queue.offer(new PairInt(0,0));
        maze.recolor(0,0, TEMPORARY);
        while(!queue.isEmpty()){
            PairInt pairInt = queue.poll();
            int x = pairInt.getX();
            int y = pairInt.getY();
            PairInt left = new PairInt(x - 1, y);
            if(isValidPair(left) &&
                    maze.getColor(x - 1, y).equals(NON_BACKGROUND)){
                maze.recolor(x - 1, y, TEMPORARY);
                trace[y * col + x - 1] = y * col + x;
                queue.offer(left);
            }

            PairInt right = new PairInt(x + 1, y);
            if(isValidPair(right) &&
                    maze.getColor(x + 1, y).equals(NON_BACKGROUND)){
                maze.recolor(x + 1, y, TEMPORARY);
                trace[y * col + x + 1] = y * col + x;
                queue.offer(right);
            }

            PairInt up = new PairInt(x, y - 1);
            if(isValidPair(up) &&
                    maze.getColor(x, y - 1).equals(NON_BACKGROUND)){
                maze.recolor(x, y - 1, TEMPORARY);
                trace[(y - 1) * col + x] = y * col + x;
                queue.offer(up);
            }

            PairInt down = new PairInt(x, y + 1);
            if(isValidPair(down) &&
                    maze.getColor(x, y + 1).equals(NON_BACKGROUND)){
                maze.recolor(x, y + 1, TEMPORARY);
                trace[(y + 1) * col + x] = y * col + x;
                queue.offer(down);
            }
        }
        return getShortesList(trace);
    }

    private ArrayList<PairInt> getShortesList(int[] trace) {
        LinkedList<PairInt> res = new LinkedList<PairInt>();
        int tmp = maze.getNRows() * maze.getNCols() - 1;
        if(trace[tmp] == -1) return new ArrayList<PairInt>(res);
        while(tmp != 0){
            int x = tmp % maze.getNCols();
            int y = tmp / maze.getNCols();
            res.addFirst(new PairInt(x, y));
            tmp = trace[tmp];
        }
        res.addFirst(new PairInt(0, 0));
        return new ArrayList<PairInt>(res);
    }

    public boolean isValidPair(PairInt p){
        int x = p.getX();
        int y = p.getY();
        if(x < 0 || x >= maze.getNCols() || y < 0 || y >= maze.getNRows())
            return false;
        return true;
    }

    public void printAllTrace(ArrayList<ArrayList<PairInt>> allMazePaths){
        System.out.println(allMazePaths.size());
        for(ArrayList<PairInt> list: allMazePaths){
            for(PairInt tmp: list){
                System.out.print(tmp.toString());
            }
            System.out.println();
        }
    }

    public void printShortestTrace(ArrayList<PairInt> shortest){
        for(PairInt p : shortest){
            System.out.print(p.toString());
        }
    }

    /*<exercise chapter="5" section="6" type="programming" number="2">*/
    public void resetTemp() {
        maze.recolor(TEMPORARY, BACKGROUND);
    }
    /*</exercise>*/

    /*<exercise chapter="5" section="6" type="programming" number="3">*/
    public void restore() {
        resetTemp();
        maze.recolor(PATH, BACKGROUND);
        maze.recolor(NON_BACKGROUND, BACKGROUND);
    }
    /*</exercise>*/
}
/*</listing>*/
