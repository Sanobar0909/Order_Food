package uz.pdp.lesson.bot.state.child;

import lombok.Getter;

@Getter
public enum AddFood {
    CHOOSE_FOOD_CATEGORY(null),
    FOOD_NAME(CHOOSE_FOOD_CATEGORY),
    FOOD_PRISE(FOOD_NAME),
    FOOD_PHOTO(FOOD_PRISE);

    public AddFood pervState;

    AddFood(AddFood pervState) {
        this.pervState = pervState;
    }

}
