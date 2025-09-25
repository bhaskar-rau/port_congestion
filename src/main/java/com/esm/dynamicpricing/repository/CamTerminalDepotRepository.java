package com.esm.dynamicpricing.repository;


import java.util.List;

public interface CamTerminalDepotRepository {
    List<String> findTerminalsByPortCode(String portCode);
}
