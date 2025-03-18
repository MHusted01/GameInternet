public class Person {
    String navn;
    String by;
    int loen;
    public Person(String navn,String by,int loen) {
        this.navn =navn;
        this.by = by;
        this.loen =loen;
    }
   public String getNavn() {
        return this.navn;
    };
    public String getBy() {
        return this.by;
    };

    public int getLoen() {
        return this.loen;
    };

}
