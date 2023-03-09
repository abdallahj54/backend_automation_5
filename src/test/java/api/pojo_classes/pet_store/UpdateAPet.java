package api.pojo_classes.pet_store;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class UpdateAPet {
    /**
     * {
     *   "id": 7,
     *   "category": {
     *     "id": 7,
     *     "name": "horse"
     *   },
     *   "name": "unicorn",
     *   "photoUrls": [
     *     "horseURL"
     *   ],
     *   "tags": [
     *     {
     *       "id": 175,
     *       "name": "hero"
     *     }
     *   ],
     *   "status": "available"
     * }
     */

    private int id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tags> tags;
    private String status;
}
