package ibarodf.core.meta;

public class Hyperlink {
    public final String reference;
    public final String type;
    public final String styleName;
    public final String visitedStyleName;
    
    
    public Hyperlink(String type, String  reference, String  styleName, String  visitedStyleName){
        this. reference =  reference;
        this.type = type;
        this.styleName = styleName;
        this.visitedStyleName = visitedStyleName;
    }



    public String toString(){
        return "[Reference : "+ reference + ";type : "+ type + ";Style Name : " +styleName+ ";Visited Syle Name : "+ visitedStyleName+ " ]"; 
    }
}
