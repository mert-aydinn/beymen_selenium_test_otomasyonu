🛍️ Beymen.com Selenium Web Otomasyon Projesi

Bu proje, Java, Selenium WebDriver ve TestNG kullanarak Beymen.com web sitesinde bir ürün arama, sepete ekleme ve sepetten silme sürecini otomatikleştirmektedir. Proje, Nesne Yönelimli Programlama (OOP) prensiplerine uygun olarak Page Object Model (POM) tasarım deseniyle geliştirilmiştir.

🌟 Özellikler
Beymen.com Anasayfayı Açma ve Kontrol: Web sitesinin başarıyla açılıp anasayfanın yüklendiği doğrulanır.
Cinsiyet Seçimi Pop-up Kontrolü: Varsa cinsiyet seçimi pop-up'ı otomatik olarak kapatılır.
Ürün Arama: Belirtilen arama terimlerini (parametrik olarak "kazak" ve "gömlek") kullanarak arama yapılır.
Arama Kutusunu Temizleme: İlk arama terimi girildikten sonra arama kutusu temizlenir.
Rastgele Ürün Seçimi: Arama sonuçlarından rastgele bir ürün seçilerek ürün detay sayfasına gidilir.
Rastgele Beden Seçimi: Ürün detay sayfasında varsa uygun (stokta olan) rastgele bir beden seçilir.
Ürünü Sepete Ekleme: Seçilen ürün sepete eklenir.
Ürünü Sepetten Silme: Sepete eklenen ürün sepetten silinir.
Sepetin Boş Olduğunu Doğrulama: Ürün silindikten sonra sepetin boş olduğu doğrulanır.
🛠️ Kullanılan Teknolojiler
Java 11+: Programlama dili.
Selenium WebDriver 4.x: Web otomasyonu için kütüphane.
TestNG: Test otomasyon framework'ü (testleri koşmak, parametre geçmek, raporlama için).
WebDriverManager: Tarayıcı sürücülerini otomatik olarak yönetmek için.
Apache Log4j 2: Kapsamlı ve esnek loglama için.
Maven: Proje yönetimi ve bağımlılık yönetimi için.
📐 Proje Yapısı
Proje, Page Object Model (POM) prensiplerine göre organize edilmiştir.