/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Teste;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amsgama
 */
public class Teste implements Serializable {

    private Map<Integer, List<Integer>> matriculaSchClassIDList;

    public Teste() {
        matriculaSchClassIDList = new HashMap<Integer, List<Integer>>();
    }
}

