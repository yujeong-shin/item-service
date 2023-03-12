package hello.itemservice.web.item.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final 붙은 애들의 생성자 만들어줌 (자동주입)
public class BasicItemController {
    private final ItemRepository itemRepository;

//    @Autowired //생성자가 하나면 생략 가능
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    //상품 등록(Get) : 상품 등록 화면만 보여줌
    @GetMapping("/add") //http주소로 들어온 값 /basic/items/add
    public String addForm() {
        return "/basic/addForm"; //반환할 뷰의 값 /templates/basic/addForm
    }

    //상품 등록(Post) : Item 객체에 값을 넣고 상품 상세 화면으로 보여줌
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * @ModelAttibute("item") Item item
     * model.addAttribute("item", item); 자동 추가
     */
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능

        return "basic/item";
    }

    /**
     * @ModelAttibute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     */
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * V1~V4에서 새로 고침하면 마지막에 전송한 POST add + 상품 데이터 계속 전송
     * 내용은 같고 ID만 다른 상품 데이터가 계속 등록된다.
     * PRG - Post/Redirect/Get
     * 뷰 템플릿("basic/item")으로 이동X 상품 상세 화면으로 redirect
     * 마지막에 전송한 내용을 GET /itemms/{itemId}로 바꿔준다.
     */
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId(); //PRG
    }

    /**
     * RedirectAttributes - V5의 URL 인코딩 안되는 문제 개선
     * URL 인코딩, pathVariable, 쿼리 파라미터까지 처리
     * 상품 등록 시 상품 상세 화면에 "저장 완료!" 출력
     * 그냥 상품 상세 화면에 들어갔으면 출력X
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
        //basic/items/3?status=true
        //pathVariable 바인딩 : {itemId}, 나머지는 쿼리 파라미터로 처리 : status=true
    }


    //상품 수정(Get) : 상품 수정 화면만 보여줌
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    //상품 수정(Post) : Item 객체의 값을 수정하고 상품 상세 화면으로 보여줌
    //수정에서도 redirect를 사용했지만, redirect는 등록에서는 정말 중요하다 !
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }


}
