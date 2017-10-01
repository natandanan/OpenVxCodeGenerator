package GUI.Model;

import BE.Kernel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public  class Model {

    Cell graphParent;

    List<Cell> allCells;
    List<Cell> addedCells;
    List<Cell> removedCells;

    List<Edge> allEdges;
    List<Edge> addedEdges;
    List<Edge> removedEdges;

    Map <Integer,Cell> cellMap; // <id,cell>
    
    private Cell source;
    private boolean isSourceSelected = false;

    public Model() {

        // graphParent = new Cell(0,null/*No kernel*/);

         // clear model, create lists
         clear();
    }

    public void clear() {

        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();

        cellMap = new HashMap<>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public List<Cell> getAddedCells() {
        return addedCells;
    }

    public List<Cell> getRemovedCells() {
        return removedCells;
    }

    public List<Cell> getAllCells() {
        return allCells;
    }

    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    //TODO need to change - 
    public void addCell(Integer id, Kernel kernel) {
        Cell Cell = new Cell(id,kernel);
        addCell(Cell);
    }

    private void addCell( Cell cell) {
        addedCells.add(cell);
        cellMap.put( cell.getCellId(), cell);
    }
    
   public void removeCell(Cell cell){
        removedCells.add(cell);
        //Delete all edges connected to this cell
        getAllEdges().forEach((t) -> {
            if (t.source == cell || t.target == cell)
                removeEdge(t);
        });
        cellMap.remove(cell.cellId);
    }
    

    public void addEdge( Integer sourceId, Integer targetId,String labelContent) {

        Cell sourceCell = cellMap.get( sourceId);
        Cell targetCell = cellMap.get( targetId);

        Edge edge = new Edge( sourceCell, targetCell,labelContent);

        addedEdges.add( edge);

    }
    
    public void removeEdge(Edge edge){
        removedEdges.add(edge);
    }

    /**
     * Attach all cells which don't have a parent to graphParent 
     * @param cellList
     */
    public void attachOrphansToGraphParent( List<Cell> cellList) {
        if (graphParent.equals(null))
            graphParent = new Cell(0,null/*No kernel*/);
        for( Cell cell: cellList) {
            if( cell.getCellParents().size() == 0) {
                graphParent.addCellChild( cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     * @param cellList
     */
    public void disconnectFromGraphParent( List<Cell> cellList) {
        if (graphParent.equals(null))
            graphParent = new Cell(0,null/*No kernel*/);
        for( Cell cell: cellList) {
            graphParent.removeCellChild(cell);
        }
    }
    
    public void disconnectRemovedCellfromParentsOrChildren(Cell removedCell){
        getAllCells().forEach((cell) -> {
            if (cell.getCellParents().contains(removedCell))
                cell.getCellParents().remove(removedCell);
            if (cell.getCellChildren().contains(removedCell))
                cell.getCellChildren().remove(removedCell);
        });
    }
    
    public void merge() {

        // cells
        allCells.addAll( addedCells);
        removedCells.forEach((cell) -> {
            disconnectRemovedCellfromParentsOrChildren(cell);
        });
        allCells.removeAll( removedCells);
        
        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll( addedEdges);
        allEdges.removeAll( removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }

    /**
     * @return the source
     */
    public Cell getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Cell source) {
        this.source = source;
    }

    /**
     * @return the isSourceSelected
     */
    public boolean isSourceSelected() {
        return isSourceSelected;
    }

    /**
     * @param isSourceSelected the isSourceSelected to set
     */
    public void setIsSourceSelected(boolean isSourceSelected) {
        this.isSourceSelected = isSourceSelected;
    }



}