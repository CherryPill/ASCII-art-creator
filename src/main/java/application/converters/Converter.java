package application.converters;

import application.dto.InputInfoDto;

@FunctionalInterface
public interface Converter {
    void convert(InputInfoDto inputInfoDto);
}
