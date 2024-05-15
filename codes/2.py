from selenium import webdriver
from selenium.webdriver.edge.service import Service
from selenium.webdriver.edge.options import Options
from selenium.webdriver.common.by import By
import time

edge_options = Options()
edge_options.add_experimental_option("debuggerAddress", "127.0.0.1:9222")

s = Service("C:\\Users\\Zhi.Xin\\Personal\\msedgedriver.exe")

driver = webdriver.Edge(options=edge_options, service=s)

driver.get('https://www.bing.com') 

# driver.execute_script("window.open('https://www.bing.com', '_blank')")
time.sleep(2)  # 稍等两秒，确保新标签页已经加载

# handles = driver.window_handles

# driver.switch_to.window(handles[1])

# time.sleep(2)  # 稍等两秒，确保新标签页已经加载
search_box = driver.find_element(By.XPATH, '//*[@id="sb_form_q"]')
search_box.send_keys('abc')
search_box.submit()

# driver.switch_to.window(handles[0])