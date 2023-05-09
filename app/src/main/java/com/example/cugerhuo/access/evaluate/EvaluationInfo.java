package com.example.cugerhuo.access.evaluate;

import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.commerce.Commerce;

/**
 * @author carollkarry
 */
public class EvaluationInfo {

    private Evaluation evaluation;
    private Commerce commerce;
    private Commodity commodity;


    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Commerce getCommerce() {
        return commerce;
    }

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }
}
