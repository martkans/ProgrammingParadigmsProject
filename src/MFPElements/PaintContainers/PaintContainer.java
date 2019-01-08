package MFPElements.PaintContainers;

abstract public class PaintContainer  {
    private Color color;
    private int paintLevel;

    protected PaintContainer(Color color, int paintLevel) {
        this.color = color;
        this.paintLevel = paintLevel;
    }

    public Color getColor() {
        return color;
    }

    public int getPaintLevel() {
        return paintLevel;
    }

    public void chargePaint(){
        paintLevel--;
    }

}
