/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package excel;

/**
 *
 * @author Amvboas
 */
public class MergeRegion {

    Integer firstRow;
    Integer lastRow;
    Integer firstColunm;
    Integer lastColumn;

    public MergeRegion(Integer firstRow, Integer lastRow, Integer firstColunm, Integer lastColumn) {
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstColunm = firstColunm;
        this.lastColumn = lastColumn;
    }



    public Integer getFirstColunm() {
        return firstColunm;
    }

    public void setFirstColunm(Integer firstColunm) {
        this.firstColunm = firstColunm;
    }

    public Integer getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(Integer firstRow) {
        this.firstRow = firstRow;
    }

    public Integer getLastColumn() {
        return lastColumn;
    }

    public void setLastColumn(Integer lastColumn) {
        this.lastColumn = lastColumn;
    }

    public Integer getLastRow() {
        return lastRow;
    }

    public void setLastRow(Integer lastRow) {
        this.lastRow = lastRow;
    }




}
