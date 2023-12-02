//
//  NaznachenieRabotView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 02.12.2023.
//

import SwiftUI

struct NaznachenieRabotView: View {
    @State private var selectedMasterId: Int?
    @State private var selectedCarId: Int?
    @State private var selectedServiceId: Int?
    @State private var dateWork = Date()

    @State private var masters: [Master] = []
    @State private var cars: [Car] = []
    @State private var services: [Service] = []

    var body: some View {
        NavigationView {
            Form {
                Picker("Мастер", selection: $selectedMasterId) {
                    ForEach(masters, id: \.id) { master in
                        Text(master.name).tag(master.id)
                    }
                }
                Picker("Автомобиль", selection: $selectedCarId) {
                    ForEach(cars, id: \.id) { car in
                        Text(car.num).tag(car.id)
                    }
                }
                Picker("Услуга", selection: $selectedServiceId) {
                    ForEach(services, id: \.id) { service in
                        Text(service.name).tag(service.id)
                    }
                }
                DatePicker("Дата работы", selection: $dateWork, displayedComponents: .date)
                Button("Назначить работу") {
                    createWork()
                }
            }
            .navigationTitle("Назначение Работ")
            .onAppear(perform: loadData)
        }
    }

    private func loadData() {
        MasterNetworkManager.shared.fetchMasters { fetchedMasters in
            DispatchQueue.main.async {
                self.masters = fetchedMasters
            }
        }
        
        CarNetworkManager.shared.fetchAllCars { fetchedCars in
            DispatchQueue.main.async {
                self.cars = fetchedCars
            }
        }

        ServiceNetworkManager.shared.fetchAllServices { fetchedServices in
            DispatchQueue.main.async {
                self.services = fetchedServices
            }
        }
    }

    private func createWork() {
        
        print("Выбранный ID мастера: \(selectedMasterId ?? 0)")
        print("Выбранный ID автомобиля: \(selectedCarId ?? 0)")
        print("Выбранный ID услуги: \(selectedServiceId ?? 0)")
        
        guard let masterId = selectedMasterId, let carId = selectedCarId, let serviceId = selectedServiceId else {
            print("Выберите все параметры")
            return
        }

        // Создаем новые объекты WorkMaster, WorkCar, и WorkService
        let master = WorkMaster(id: masterId)
        let car = WorkCar(id: carId)
        let service = WorkService(id: serviceId)

        // Используем эти объекты для создания объекта Work
        let newWork = Work(master: master, car: car, service: service, dateWork: dateWork)
        WorkNetworkManager.shared.addWork(work: newWork) { success in
            if success {
                print("Работа успешно назначена")
            } else {
                print("Ошибка при назначении работы")
            }
        }
    }
}

