/**
 * represents a request that has a type(get, patch, ...) and a name
 *
 * @author Mohammad Salehi Vaziri
 */
public class Request {
    private String name;
    private String type;

    /**
     * Instantiates a new Request.
     *
     * @param name the name
     * @param type the type
     */
    public Request(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }
}
