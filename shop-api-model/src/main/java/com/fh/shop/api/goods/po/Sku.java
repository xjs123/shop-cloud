package com.fh.shop.api.goods.po;


import java.io.Serializable;
import java.math.BigDecimal;


public class


Sku implements Serializable {

  private Long id;

  private String skuName;

  private  Long spuId;

  private BigDecimal price;

  private  int stock;

  private  String specInfo;

  private String image;

  private  Long colorId;

  private String status;

  private String  newOld;

  private  String  hot;

  private int sales;

  public int getSales() {
    return sales;
  }

  public void setSales(int Sales) {
    this.sales = sales;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSkuName() {
    return skuName;
  }

  public void setSkuName(String skuName) {
    this.skuName = skuName;
  }

  public Long getSpuId() {
    return spuId;
  }

  public void setSpuId(Long spuId) {
    this.spuId = spuId;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public String getSpecInfo() {
    return specInfo;
  }

  public void setSpecInfo(String specInfo) {
    this.specInfo = specInfo;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Long getColorId() {
    return colorId;
  }

  public void setColorId(Long colorId) {
    this.colorId = colorId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getNewOld() {
    return newOld;
  }

  public void setNewOld(String newOld) {
    this.newOld = newOld;
  }

  public String getHot() {
    return hot;
  }

  public void setHot(String hot) {
    this.hot = hot;
  }
}
