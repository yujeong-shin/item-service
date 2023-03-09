package hello.itemservice.domain.item;

import lombok.Data;

@Data // @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
public class Item {
    private Long id;
    private String itemName;
    private Integer price; // null 값도 저장하기 위해
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
