package hello.itemservice.web.basic;

import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final 붙은 애들의 생성자 만들어줌 (자동주입)
public class BasicItemController {
    private final ItemRepository itemRepository;

//    @Autowired //생성자가 하나면 생략 가능
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

}
