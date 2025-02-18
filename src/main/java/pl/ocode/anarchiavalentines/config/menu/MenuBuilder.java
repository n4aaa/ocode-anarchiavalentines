package pl.ocode.anarchiavalentines.config.menu;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MenuBuilder extends OkaeriConfig {
    private String title;
    private int rows;
    private List<MenuItem> items;
}