//
//  CarListView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 26.11.2023.
//

import SwiftUI

struct CarListView: View {
    @State private var cars: [Car] = []
    @State private var showingAddCarView = false

    var body: some View {
        List {
            ForEach(cars, id: \.id) { car in
                NavigationLink(destination: EditCarView(car: car, onCarUpdated: fetchCars)) {
                    Text(car.num)
                }
            }
            .onDelete(perform: deleteCar)
        }
        .navigationTitle("Автомобили")
        .toolbar {
            ToolbarItemGroup(placement: .navigationBarTrailing) {
                Button(action: fetchCars) {
                    Text("Обновить")
                }
                Button(action: { showingAddCarView = true }) {
                    Image(systemName: "plus")
                }
            }
        }
        .sheet(isPresented: $showingAddCarView, onDismiss: fetchCars) {
            AddCarView(cars: $cars)
        }
    }

    private func fetchCars() {
        CarNetworkManager.shared.fetchAllCars { fetchedCars in
            self.cars = fetchedCars
        }
    }
    
    private func deleteCar(at offsets: IndexSet) {
        offsets.forEach { index in
            let carId = cars[index].id ?? 0
            CarNetworkManager.shared.deleteCar(carId: carId) { success in
                if success {
                    DispatchQueue.main.async {
                        cars.remove(at: index)
                    }
                } else {
                    // Обработка ошибок
                }
            }
        }
    }
}


