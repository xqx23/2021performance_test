package cn.edu.xmu.oomall.other.model.bo;

/**
 * @author jxy
 * @create 2021/12/12 8:15 PM
 */

public abstract class BaseShareRebate {

    abstract public Long calculate();

    protected Long calculatePart(Integer level, Integer nextLevel, Long amount, Integer unit, Long price, Integer percentage) {
        if (amount <= level) {
            return 0L;
        } else if (nextLevel.equals(-1) || amount <= nextLevel) {
            return (amount - level + unit - 1) / unit * price * percentage;
        } else {
            return (nextLevel - level + unit - 1) / unit * price * percentage;
        }
    }
}
