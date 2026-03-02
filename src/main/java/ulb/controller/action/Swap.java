package ulb.controller.action;
import ulb.model.bugemon.Bugemon;

public class Swap implements Action {
    private Bugemon toSwap;

    public Swap(){}
    public Swap(Bugemon toSwap){
        this.toSwap = toSwap;
    }

    public Bugemon getToSwap() {return this.toSwap;}

    public void setToSwap(Bugemon toSwap) {this.toSwap = toSwap;}
}
